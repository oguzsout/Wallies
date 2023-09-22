package com.oguzdogdu.wallieshd.presentation.downloadphoto

sealed class DownloadPhotoState {
    data class PhotoQuality(val quality: String?) : DownloadPhotoState()
}
