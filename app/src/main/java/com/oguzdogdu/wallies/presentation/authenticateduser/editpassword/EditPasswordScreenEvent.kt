package com.oguzdogdu.wallies.presentation.authenticateduser.editpassword

sealed class EditPasswordScreenEvent {
    data class UserPassword(
        val password: String? = null
    ) : EditPasswordScreenEvent()
}
