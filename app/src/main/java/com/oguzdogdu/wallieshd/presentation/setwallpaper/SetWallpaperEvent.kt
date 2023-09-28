package com.oguzdogdu.wallieshd.presentation.setwallpaper

sealed interface SetWallpaperEvent {
    data class AdjustWallpaper(val place: String? = null) :
        SetWallpaperEvent

    data class StatusOfAdjustWallpaper(val isCompleted: Boolean?, val message: String? = null) :
        SetWallpaperEvent
}
