plugins {
    alias(libs.plugins.kotlin.jvm)
    java
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(project(":core"))

    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.kotest.assertions.core)
}
