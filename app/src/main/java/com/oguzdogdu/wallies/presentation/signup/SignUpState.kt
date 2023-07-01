package com.oguzdogdu.wallies.presentation.signup

import com.oguzdogdu.wallies.presentation.login.LoginState

sealed class SignUpState {
    object Start : SignUpState()
    object Loading : SignUpState()
    data class ErrorSignUp(val errorMessage: String) : SignUpState()
    object UserSignUp : SignUpState()
}
