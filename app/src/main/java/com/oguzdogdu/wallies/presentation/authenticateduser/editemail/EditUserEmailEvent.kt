package com.oguzdogdu.wallies.presentation.authenticateduser.editemail

sealed class EditUserEmailEvent {
    data class ChangedEmail(val email: String?, val password: String?) : EditUserEmailEvent()
}
