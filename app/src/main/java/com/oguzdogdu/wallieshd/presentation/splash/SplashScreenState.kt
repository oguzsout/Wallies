package com.oguzdogdu.wallieshd.presentation.splash

sealed class SplashScreenState {
    object StartFlow : SplashScreenState()
    object UserSignedIn : SplashScreenState()
    object UserNotSigned : SplashScreenState()
}
