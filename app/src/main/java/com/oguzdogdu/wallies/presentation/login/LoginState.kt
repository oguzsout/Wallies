package com.oguzdogdu.wallies.presentation.login

sealed class LoginState {
    object Start : LoginState()
    object Loading : LoginState()
    data class ErrorSignIn(val errorMessage: String) : LoginState()
    object UserSignIn : LoginState()
    object UserNotSignIn : LoginState()
}
