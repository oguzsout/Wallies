package com.oguzdogdu.wallieshd.presentation.authenticateduser.editemail

sealed class EditEmailScreenState {
    object Loading : EditEmailScreenState()
    data class UserInfoError(val errorMessage: String?) : EditEmailScreenState()
    data class UserEmail(
        val email: String? = null
    ) : EditEmailScreenState()
}
