package com.oguzdogdu.wallieshd.presentation.setwallpaper

sealed class SetWallpaperState {
    data class SetWallpaper(val finallyPlace: String?) : SetWallpaperState()
    data class SuccessAdjustImage(val isCompleted: Boolean?, val message: String? = null) : SetWallpaperState()
}
