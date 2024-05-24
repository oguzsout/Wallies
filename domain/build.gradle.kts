@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "com.oguzdogdu.domain"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        targetSdk = 33
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = ("17")
    }
}

dependencies {

    implementation(libs.google.dagger.hilt)
    implementation(libs.androidx.monitor)
    implementation(libs.androidx.junit.ktx)
    ksp(libs.google.dagger.hilt.compiler)
    implementation ("com.google.firebase:firebase-auth-ktx:22.1.0")
    implementation ("com.google.firebase:firebase-bom:32.2.0")
    implementation ("com.google.firebase:firebase-firestore-ktx:24.7.0")
    implementation(libs.androidx.paging.runtime)
    testImplementation(libs.androidx.paging.common)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
}