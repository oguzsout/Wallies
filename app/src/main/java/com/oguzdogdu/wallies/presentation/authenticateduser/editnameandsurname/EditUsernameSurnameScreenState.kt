package com.oguzdogdu.wallies.presentation.authenticateduser.editnameandsurname

sealed class EditUsernameSurnameScreenState {
    object Loading : EditUsernameSurnameScreenState()
    data class UserInfoError(val errorMessage: String?) : EditUsernameSurnameScreenState()
    data class UserInfos(
        val name: String? = null,
        val surname: String? = null,
        val email: String? = null,
        val profileImage: String? = null
    ) : EditUsernameSurnameScreenState()
}
