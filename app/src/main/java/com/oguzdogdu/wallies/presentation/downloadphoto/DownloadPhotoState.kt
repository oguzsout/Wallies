package com.oguzdogdu.wallies.presentation.downloadphoto

sealed class DownloadPhotoState {
    data class PhotoQuality(val quality: String?) : DownloadPhotoState()
}
