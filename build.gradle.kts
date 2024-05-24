buildscript {
    dependencies {
        classpath(libs.kotlin.gradle.plugin)
        classpath("com.google.gms:google-services:4.3.15")
    }
}
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.navigation.safe.args) apply false
    kotlin("plugin.serialization") version "1.8.10" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
}

task("clean") {
    delete(project.buildDir)
}