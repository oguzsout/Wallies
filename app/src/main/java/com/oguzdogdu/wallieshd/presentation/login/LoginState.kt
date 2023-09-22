package com.oguzdogdu.wallieshd.presentation.login

sealed class LoginState {
    object Start : LoginState()
    object Loading : LoginState()
    data class ButtonEnabled(val isEnabled: Boolean) : LoginState()
    data class ErrorSignIn(val errorMessage: String) : LoginState()
    object UserSignIn : LoginState()
    object UserNotSignIn : LoginState()
}
