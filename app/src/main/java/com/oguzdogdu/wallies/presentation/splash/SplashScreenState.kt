package com.oguzdogdu.wallies.presentation.splash

sealed class SplashScreenState {
    object StartFlow : SplashScreenState()
    object UserSignedIn : SplashScreenState()
    object UserNotSigned : SplashScreenState()
}
