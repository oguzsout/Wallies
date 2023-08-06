package com.oguzdogdu.wallies.presentation.authenticateduser.editprofilephoto

sealed class EditProfilePhotoEvent {
    object ChangeProfileImage : EditProfilePhotoEvent()
}
