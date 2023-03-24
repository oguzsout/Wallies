package com.oguzdogdu.buildsrc

object Dependencies {
    object Jvm {
        const val target = "1.8"
        const val inject = "1"
    }

    object Sdk {
        const val min = 21
        const val target = 31
        const val compile = 31
    }

    object Kotlin {
        const val compiler = "1.5.31"
        const val gradle = "1.6.10"
        const val coroutines = "1.6.0"
        const val coroutineScope = "2.4.0"
    }

    object BuildTools {
        const val gradle = "7.1.3"
    }

    object AndroidX {
        const val core = "1.7.0"
        const val appcompat = "1.4.1"
        const val constraintLayout = "2.1.3"
        const val legacySupport = "1.0.0"
        const val navigation = "2.4.1"
        const val room = "2.5.0"
        const val paging = "3.1.0"
    }

    object Google {
        const val hilt = "2.40.5"
        const val hiltCompiler = "1.0.0"
        const val material = "1.5.0"
    }

    object Network {
        const val retrofit = "2.9.0"
        const val okhttpLogging = "4.9.1"
        const val gson = "2.9.0"
    }

    object Testing {
        const val junitExt = "1.1.3"
        const val espresso = "3.4.0"
        const val junit = "4.13.2"
    }

    object Image {
        const val coil = "2.2.0"
        const val lottie = "3.5.0"
    }
}