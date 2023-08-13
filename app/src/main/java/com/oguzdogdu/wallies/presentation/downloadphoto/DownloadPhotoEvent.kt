package com.oguzdogdu.wallies.presentation.downloadphoto

sealed class DownloadPhotoEvent {
    object ClickedRaw : DownloadPhotoEvent()
    object ClickedFull : DownloadPhotoEvent()
    object ClickedMedium : DownloadPhotoEvent()
    object ClickedLow : DownloadPhotoEvent()
}
