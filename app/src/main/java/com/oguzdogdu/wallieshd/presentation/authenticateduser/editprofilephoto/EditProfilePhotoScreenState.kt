package com.oguzdogdu.wallieshd.presentation.authenticateduser.editprofilephoto

sealed class EditProfilePhotoScreenState {
    object Loading : EditProfilePhotoScreenState()
    data class ProcessCompleted(val isCompleted: Boolean?) : EditProfilePhotoScreenState()
    data class UserInfoError(val errorMessage: String?) : EditProfilePhotoScreenState()
    data class ProfileImage(val image: String?) : EditProfilePhotoScreenState()
}
