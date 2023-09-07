package com.oguzdogdu.wallies.presentation.signup

import android.net.Uri

sealed class SignUpScreenEvent {
    object ButtonState : SignUpScreenEvent()
    data class UserInfosForSignUp(
        val name: String,
        val surname: String,
        val email: String,
        val password: String,
        val photoUri: Uri
    ) : SignUpScreenEvent()
}
