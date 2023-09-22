package com.oguzdogdu.wallieshd.presentation.setwallpaper

sealed class SetWallpaperEvent {
    data class AdjustWallpaper(val place: String? = null) : SetWallpaperEvent()
}
