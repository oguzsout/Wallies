package com.oguzdogdu.wallies.presentation.login.forgotmypassword

sealed class ForgotPasswordScreenEvent {
    data class SendEmail(
        val email: String? = null
    ) : ForgotPasswordScreenEvent()
}
