import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.FileInputStream
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("org.jlleitschuh.gradle.ktlint") version "11.3.2"
}
val keystorePropertiesFile: File = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))
val apiKey: String = gradleLocalProperties(rootDir).getProperty("API_KEY")

android {
    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }
    compileSdk = 33
    namespace = "com.oguzdogdu.wallieshd"
    defaultConfig {
        applicationId = "com.oguzdogdu.wallieshd"
        minSdk = 21
        targetSdk = 33
        versionCode = 3
        versionName = "1.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "API_KEY", apiKey)
        signingConfig = signingConfigs.getByName("debug")
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = ("17")
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.6"
    }
}

tasks.getByPath("preBuild").dependsOn("ktlintFormat")

ktlint {
    android.set(true)
    ignoreFailures.set(true)
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.SARIF)
    }
    outputToConsole.set(true)
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":cache"))
    implementation(project(":network"))

    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material)
    implementation(libs.androidx.contraintlayout)
    implementation(libs.androidx.test.core)
    implementation("com.google.firebase:firebase-messaging-ktx:23.2.0")
    implementation("com.google.firebase:firebase-auth-ktx:22.1.0")
    implementation("com.google.firebase:firebase-bom:32.2.0")
    implementation("com.google.firebase:firebase-firestore-ktx:24.7.0")
    implementation("com.google.firebase:firebase-storage:20.2.1")
    implementation("com.google.firebase:firebase-config-ktx:21.4.1")
    implementation(libs.play.services.auth)

    testImplementation(libs.androidx.junit)
    testImplementation("junit:junit:4.12")
    androidTestImplementation(libs.androidx.junit.ext)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("androidx.compose.ui:ui:1.4.3")
    implementation("androidx.compose.runtime:runtime:1.4.3")

    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    implementation("androidx.paging:paging-runtime:3.1.1")
    implementation("androidx.paging:paging-compose:1.0.0-alpha19")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.26.3-beta")

    implementation("androidx.compose.material:material:1.4.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.1")
    implementation("io.coil-kt:coil-compose:2.3.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("com.github.skydoves:balloon:1.5.4")

    implementation(libs.android.compose.bom)

    testImplementation("io.mockk:mockk:1.12.3")
    testImplementation("org.mockito:mockito-inline:3.8.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.11.0")
    testImplementation("androidx.arch.core:core-testing:2.2.0")

    // Truth
    testImplementation("com.google.truth:truth:1.1.4")
    // MockK
    testImplementation("io.mockk:mockk:1.12.4")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.9")

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    implementation(libs.google.firebase.bom)

    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.preference)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter)

    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)

    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.fragment.ktx)

    implementation(libs.androidx.paging.runtime)
    testImplementation(libs.androidx.paging.common)

    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)
    implementation(libs.kotlin.coil)

    implementation(libs.google.dagger.hilt)
    kapt(libs.google.dagger.hilt.compiler)

    implementation(libs.androidx.swipe.refresh)
    implementation(libs.glide)
    implementation(libs.photo.view)
}
