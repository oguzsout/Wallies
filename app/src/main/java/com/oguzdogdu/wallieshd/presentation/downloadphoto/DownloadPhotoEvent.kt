package com.oguzdogdu.wallieshd.presentation.downloadphoto

sealed class DownloadPhotoEvent {
    object ClickedRaw : DownloadPhotoEvent()
    object ClickedFull : DownloadPhotoEvent()
    object ClickedMedium : DownloadPhotoEvent()
    object ClickedLow : DownloadPhotoEvent()
}
