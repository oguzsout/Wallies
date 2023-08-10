package com.oguzdogdu.wallies.presentation.signup

sealed class SignUpState {
    object Start : SignUpState()
    object Loading : SignUpState()
    data class ButtonEnabled(val isEnabled: Boolean) : SignUpState()
    data class ErrorSignUp(val errorMessage: String) : SignUpState()
    object UserSignUp : SignUpState()
}
