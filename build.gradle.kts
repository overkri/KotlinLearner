import org.gradle.api.tasks.testing.Test

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.kotlin.allopen) apply false
    alias(libs.plugins.kotlin.noarg) apply false
    alias(libs.plugins.spring.boot) apply false
}

allprojects {
    group = "ledger"
    version = "1.0.0"
}

val moduleFilter = providers.gradleProperty("module").orNull

subprojects {
    tasks.withType<Test>().configureEach {
        useJUnitPlatform {
            if (moduleFilter != null) {
                includeTags("m$moduleFilter")
            }
        }
        testLogging {
            events("passed", "skipped", "failed")
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
        // Integration tests in this course share Docker fixtures; keep execution deterministic.
        maxParallelForks = 1
        systemProperty("testcontainers.reuse.enable", "true")
        systemProperty("file.encoding", "UTF-8")
    }
}
