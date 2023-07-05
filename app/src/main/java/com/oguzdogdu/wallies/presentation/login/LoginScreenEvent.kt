package com.oguzdogdu.wallies.presentation.login

sealed class LoginScreenEvent {
    data class UserSignIn(val email: String, val password: String) : LoginScreenEvent()
}
