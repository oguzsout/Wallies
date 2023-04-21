import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
}
val apiKey: String = gradleLocalProperties(rootDir).getProperty("API_KEY")
android {
    compileSdk = 33
    namespace = "com.oguzdogdu.wallies"
    defaultConfig {
        applicationId = "com.oguzdogdu.wallies"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "API_KEY", apiKey)
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material)
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(libs.androidx.test.core)
    testImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.junit.ext)
    androidTestImplementation(libs.androidx.espresso.core)

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // Coroutine Lifecycle Scopes
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.preference)

    //retrofit
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter)

    //okhttp
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)

    //navigation
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.fragment.ktx)

    //paging
    implementation(libs.androidx.paging.runtime)
    testImplementation(libs.androidx.paging.common)

    // Activity KTX for viewModels()
    implementation(libs.androidx.activity)
    implementation (libs.androidx.fragment)
    implementation(libs.kotlin.coil)

    //Dagger - Hilt
    implementation(libs.google.dagger.hilt)
    kapt(libs.google.dagger.hilt.compiler)

    //room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation(libs.androidx.swipe.refresh)
    implementation(libs.glide)
    implementation(libs.photo.view)
}