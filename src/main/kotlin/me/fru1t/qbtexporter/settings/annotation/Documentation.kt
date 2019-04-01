package me.fru1t.qbtexporter.settings.annotation

/**
 * Denotes the documentation for a specific setting which is shown as plain text to the end user
 * when requested.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Documentation(val documentation: String)
