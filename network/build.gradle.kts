import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}
val debugApiKey: String = gradleLocalProperties(rootDir).getProperty("DEBUG_API_KEY")
val releaseApiKey: String = gradleLocalProperties(rootDir).getProperty("RELEASE_API_KEY")
android {
    namespace = "com.oguzdogdu.network"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        targetSdk = 33
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        buildTypes {
            debug {
                buildConfigField ("String", "API_KEY", debugApiKey)
            }
            release {
                buildConfigField ("String", "API_KEY", releaseApiKey)
                isMinifyEnabled =  false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
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
        buildConfig = true
    }
}

dependencies {

    implementation(project(":domain"))

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    testImplementation("androidx.test:core-ktx:1.3.0")
    testImplementation("androidx.test:core:1.3.0")
    testImplementation( "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("io.mockk:mockk:1.12.3")
    testImplementation("org.mockito:mockito-inline:3.8.0")
    testImplementation("app.cash.turbine:turbine:0.4.0")
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.11.0"))
    testImplementation ("com.squareup.okhttp3:mockwebserver:4.11.0")
        testImplementation("androidx.arch.core:core-testing:2.2.0")

        //Truth
        testImplementation ("com.google.truth:truth:1.1.4")
        //MockK
        testImplementation ("io.mockk:mockk:1.12.4")

    debugImplementation("com.github.chuckerteam.chucker:library:4.0.0")
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:4.0.0")

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)

    implementation(libs.google.dagger.hilt)
    ksp(libs.google.dagger.hilt.compiler)
}