package com.oguzdogdu.wallies.presentation.setwallpaper

sealed class SetWallpaperEvent {
    data class AdjustWallpaper(val place: String? = null) : SetWallpaperEvent()
}
