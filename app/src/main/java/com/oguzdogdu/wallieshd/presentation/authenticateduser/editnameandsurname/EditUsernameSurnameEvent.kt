package com.oguzdogdu.wallieshd.presentation.authenticateduser.editnameandsurname

sealed class EditUsernameSurnameEvent {
    data class ChangedUserName(val name: String?) : EditUsernameSurnameEvent()
    data class ChangedSurName(val surname: String?) : EditUsernameSurnameEvent()
}
