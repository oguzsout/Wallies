package com.oguzdogdu.wallieshd.presentation.setwallpaper

sealed class SetWallpaperState {
    data class SetWallpaper(val finallyPlace: String?) : SetWallpaperState()
}
