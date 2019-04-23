import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.jvm.tasks.Jar

group = "me.fru1t"
version = "0.9-dev"


plugins {
    application
    checkstyle
    findbugs
    jacoco

    kotlin("jvm") version "1.3.21"
    kotlin("kapt") version "1.3.21"
    id("org.jmailen.kotlinter") version "1.22.0"
}

repositories {
    maven { setUrl("http://dl.bintray.com/kotlin/kotlin-eap") }
    mavenCentral()
    jcenter()
}

dependencies {
    val daggerVersion = "2.21"
    val junit5Version = "5.3.1"
    val mockitoVersion = "2.1.0"
    val truthVersion = "0.43"
    val gsonVersion = "2.8.5"
    val ktorVersion = "1.1.3"

    fun ktor() = "io.ktor:ktor:$ktorVersion"
    fun ktor(module: String) = "io.ktor:ktor-$module:$ktorVersion"

    kapt("com.google.dagger:dagger-compiler:$daggerVersion")

    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation("com.google.dagger:dagger:$daggerVersion")
    implementation("com.google.code.gson:gson:$gsonVersion")
    implementation(ktor())
    implementation(ktor("client-core"))
    implementation(ktor("client-apache"))
    implementation(ktor("server-core"))
    implementation(ktor("server-jetty"))
    implementation("org.slf4j:slf4j-simple:1.7.26")
    implementation("javax.inject:javax.inject:1")

    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:$mockitoVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit5Version")
    testImplementation("com.google.truth:truth:$truthVersion")
    testImplementation(ktor("client-mock-jvm"))

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit5Version")
}

kotlinter {
    ignoreFailures = false
    indentSize = 2
    continuationIndentSize = 4
    //    reporter = ["checkstyle", "plain"]
    experimentalRules = false
}

tasks {
    "test"(Test::class) {
        useJUnitPlatform()

        // Always run tests, even when nothing changed.
        dependsOn("cleanTest")

        // Show results
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
    "fatJar"(Jar::class) {
        baseName = "${project.name}-fat"
        manifest {
            attributes["Implementation-Title"] = "qBittorrent Exporter"
            attributes["Implementation-Version"] = version
            attributes["Main-Class"] = "me.fru1t.qbtexporter.QbtExporterKt"
        }
        from(configurations.runtimeClasspath.map {
            if (it.isDirectory) it else zipTree(it) }
        )
        with(tasks["jar"] as CopySpec)
    }
    Unit
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application{
    mainClassName = "me.fru1t.qbtexporter.QbtExporterKt"
}
