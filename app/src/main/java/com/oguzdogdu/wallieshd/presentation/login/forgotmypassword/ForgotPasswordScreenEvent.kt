package com.oguzdogdu.wallieshd.presentation.login.forgotmypassword

sealed class ForgotPasswordScreenEvent {
    data class SendEmail(
        val email: String? = null
    ) : ForgotPasswordScreenEvent()
    object ButtonState : ForgotPasswordScreenEvent()
}
