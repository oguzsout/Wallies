package com.oguzdogdu.wallies.presentation.login

sealed class LoginScreenEvent {
    object ButtonState : LoginScreenEvent()
    data class GoogleButton(val idToken: String?) : LoginScreenEvent()
    data class UserSignIn(val email: String, val password: String) : LoginScreenEvent()
}
