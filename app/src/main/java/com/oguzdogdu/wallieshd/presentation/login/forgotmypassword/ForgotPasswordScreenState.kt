package com.oguzdogdu.wallieshd.presentation.login.forgotmypassword

sealed class ForgotPasswordScreenState {
    data class ButtonEnabled(val isEnabled: Boolean) : ForgotPasswordScreenState()
}
