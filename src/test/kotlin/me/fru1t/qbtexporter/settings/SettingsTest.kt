package me.fru1t.qbtexporter.settings

import com.google.common.truth.Truth.assertWithMessage
import me.fru1t.qbtexporter.settings.annotation.Documentation
import org.junit.jupiter.api.Test
import java.util.Stack
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

internal class SettingsTest {
  private data class PureDataClass(val test: String)

  private companion object {
    private val ALLOWED_SETTINGS_TYPES =
      listOf(
          Iterable::class,
          Map::class,
          Boolean::class,
          Char::class,
          CharArray::class,
          CharSequence::class,
          Number::class,
          Enum::class)
  }

  @Test
  fun checkDocumentationAnnotation() {
    forEachSettingsClassParameter { settingsClass: KClass<*>, parameter: KParameter ->
      assertWithMessage("No @Documentation annotation on ${format(settingsClass, parameter)}")
        .that(parameter.findAnnotation<Documentation>())
        .isNotNull()
    }
  }

  @Test
  fun checkSettingType() {
    forEachSettingType { settingsClass: KClass<*>, parameter: KParameter, type: KType ->

      var isOnAllowedList = false

      // Allowed if it's a data class
      if (type.jvmErasure.isData) {
        println(
            "Found valid setting ${format(settingsClass, parameter)}: " +
                "${type.jvmErasure.simpleName} (is a data class)")
        isOnAllowedList = true
      }

      // Allowed if it's an array
      if (type.jvmErasure.java.isArray) {
        println(
            "Found valid setting ${format(settingsClass, parameter)}: " +
                "${type.jvmErasure.simpleName} (is an array)")
        isOnAllowedList = true
      }

      // Allowed if it's on the allowed list
      if (!isOnAllowedList) {
        for (allowedType in ALLOWED_SETTINGS_TYPES) {
          if (type.jvmErasure.isSubclassOf(allowedType)) {
            println(
                "Found valid setting ${format(settingsClass, parameter)}: " +
                    "${type.jvmErasure.simpleName} (is a subclass of ${allowedType.simpleName})")
            isOnAllowedList = true
            break
          }
        }
      }

      assertWithMessage(
          "Setting type ${type.jvmErasure.qualifiedName} in not allowed in " +
              "${format(settingsClass, parameter)}. Type must be a primitive, collection, or " +
              "another data class.")
        .that(isOnAllowedList)
        .isTrue()
    }
  }

  @Test
  fun checkOptional() {
    forEachSettingsClassParameter { settingsClass: KClass<*>, parameter: KParameter ->
      assertWithMessage("Setting ${format(settingsClass, parameter)} must be optional.")
        .that(parameter.isOptional)
        .isTrue()
    }
  }

  @Test
  fun checkNullable() {
    forEachSettingsClassParameter { settingsClass: KClass<*>, parameter: KParameter ->
      assertWithMessage("Setting ${format(settingsClass, parameter)} must be nullable.")
        .that(parameter.type.isMarkedNullable)
        .isTrue()
    }
  }

  @Test
  fun checkVar() {
    forEachSettingsClassParameter { settingsClass: KClass<*>, parameter: KParameter ->
      val memberProperty =
        settingsClass.declaredMemberProperties.single { it.name == parameter.name }
      assertWithMessage("${format(settingsClass, parameter)} must be `var` instead of `val`.")
        .that(memberProperty)
        .isInstanceOf(KMutableProperty1::class.java)
    }
  }

  @Test
  fun checkPure() {
    forEachSettingsClassParameter { settingsClass: KClass<*>, _ ->
      assertWithMessage(
          "${settingsClass.qualifiedName} must be a pure data class -- it cannot have fields " +
              "declared outside the primary constructor.")
        .that(settingsClass.declaredMemberProperties.map { it.name })
        .containsExactlyElementsIn(settingsClass.primaryConstructor!!.parameters.map { it.name })

      assertWithMessage(
          "${settingsClass.qualifiedName} must be a pure data class -- it cannot have functions " +
              "declared inside the body.")
        .that(settingsClass.functions.size - settingsClass.primaryConstructor!!.parameters.size)
        .isEqualTo(
            PureDataClass::class.functions.size -
                PureDataClass::class.primaryConstructor!!.parameters.size)
    }
  }

  /** Pretty prints the given parameter in relation to its settings class. */
  private fun format(settingsClass: KClass<*>, parameter: KParameter) =
    "${settingsClass.qualifiedName}#${parameter.name}"

  private fun assertKTypeIsNonNull(
    settingsClass: KClass<*>,
    parameter: KParameter,
    kType: KType?
  ): KType {
    if (kType == null) {
      assertWithMessage(
          "Settings must be strongly typed for serialization/deserialization, and thus generics " +
              "cannot have \"<*>\" definitions.\n" +
              "Please fix at ${format(settingsClass, parameter)}")
        .fail()
    }
    return kType!!
  }

  /**
   * Runs [test] against all declared types (including those within generics) inside [Settings] and
   * all sub-settings.
   */
  private fun forEachSettingType(
    test: (settingsClass: KClass<*>, parameter: KParameter, type: KType) -> Unit
  ) {
    forEachSettingsClassParameterRecursive(
        Settings::class,
        Stack()) { settingsClass: KClass<*>, parameter: KParameter ->
      forEachSettingTypeRecursive(
          assertKTypeIsNonNull(settingsClass, parameter, parameter.type),
          settingsClass,
          parameter) { type: KType ->
        test(settingsClass, parameter, type)
      }
    }
  }

  private fun forEachSettingTypeRecursive(
    rootKType: KType,
    settingsClass: KClass<*>,
    parameter: KParameter,
    test: (type: KType) -> Unit
  ) {
    // Test the parameter itself
    test(rootKType)

    // Recursively test the parameter's parameters
    rootKType.arguments.forEach { kTypeProjection: KTypeProjection ->
      forEachSettingTypeRecursive(
          assertKTypeIsNonNull(settingsClass, parameter, kTypeProjection.type),
          settingsClass,
          parameter,
          test)
    }
  }

  /** Runs [test] against all parameters inside [Settings] and all sub-settings. */
  private fun forEachSettingsClassParameter(
    test: (settingsClass: KClass<*>, parameter: KParameter) -> Unit
  ) {
    forEachSettingsClassParameterRecursive(Settings::class, Stack(), test)
  }

  /**
   * Recursively runs [test] against all parameters inside [rootSettingsClass] and all
   * sub-settings.
   */
  private fun forEachSettingsClassParameterRecursive(
    rootSettingsClass: KClass<*>,
    visitedSettings: Stack<KClass<*>>,
    test: (settingsClass: KClass<*>, parameter: KParameter) -> Unit
  ) {
    // Stop circular dependencies
    if (visitedSettings.contains(rootSettingsClass)) {
      visitedSettings.push(rootSettingsClass)
      val dependencyChain = visitedSettings.joinToString(
          separator = " depends on... \n\t",
          prefix = "\t",
          postfix = " (this is the first element).")
      assertWithMessage(
          "Found a circular dependency in the settings chain:\n$dependencyChain").fail()
    }

    // Stop generic settings. Too lazy to implement as it's not a common use-case. The issue is when
    // attempting to get the type of a generic field, the jvmErasure will return Any::class. Instead
    // we need to type check against the generic type.
    if (rootSettingsClass.typeParameters.isNotEmpty()) {
      assertWithMessage(
          "Generic setting classes is not supported. Please fix ${rootSettingsClass.qualifiedName}")
        .fail()
    }

    visitedSettings.push(rootSettingsClass)

    val parameters = rootSettingsClass.primaryConstructor!!.parameters
    parameters.forEach { parameter ->
      // Test the parameter itself
      test(rootSettingsClass, parameter)

      // Recursively call if there are any data classes within generic types for this parameter
      forEachSettingTypeRecursive(parameter.type, rootSettingsClass, parameter) { type: KType ->
        val parameterClass = type.jvmErasure
        if (parameterClass.isData) {
          forEachSettingsClassParameterRecursive(parameterClass, visitedSettings, test)
        }
      }
    }

    visitedSettings.pop()
  }
}
