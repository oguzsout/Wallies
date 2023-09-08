package com.oguzdogdu.wallies.presentation.authenticateduser.editprofilephoto

import android.net.Uri

sealed class EditProfilePhotoEvent {
    data class ChangeProfileImage(val photoUri: Uri?) : EditProfilePhotoEvent()
}
