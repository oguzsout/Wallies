package com.oguzdogdu.wallies.presentation.authenticateduser.editnameandsurname

sealed class EditUsernameSurnameEvent {
    data class ChangedUserName(val name: String?) : EditUsernameSurnameEvent()
    data class ChangedSurName(val surname: String?) : EditUsernameSurnameEvent()
}
