plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.oguzdogdu.network"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    implementation(project(":domain"))

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")

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
    kapt(libs.google.dagger.hilt.compiler)
}