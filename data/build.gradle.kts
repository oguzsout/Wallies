@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "com.oguzdogdu.data"
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
    implementation(project(":domain"))
    implementation(project(":cache"))
    implementation(project(":network"))


    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material)
    implementation(libs.androidx.test.core)
    implementation(libs.play.services.auth)
    testImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.junit.ext)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.google.firebase:firebase-messaging-ktx:23.2.0")
    implementation("com.google.firebase:firebase-auth-ktx:22.1.0")
    implementation("com.google.firebase:firebase-bom:32.2.0")
    implementation("com.google.firebase:firebase-firestore-ktx:24.7.0")
    implementation(libs.google.firebase.bom)

    debugImplementation("com.github.chuckerteam.chucker:library:4.0.0")
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:4.0.0")

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)

    implementation(libs.google.dagger.hilt)
    ksp(libs.google.dagger.hilt.compiler)

    implementation(libs.androidx.paging.runtime)
    testImplementation(libs.androidx.paging.common)
}