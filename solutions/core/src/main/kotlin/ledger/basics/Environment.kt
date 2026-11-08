package ledger.basics

fun environmentSummary(): String =
    "Kotlin ${KotlinVersion.CURRENT} / JVM ${System.getProperty("java.specification.version")}"
