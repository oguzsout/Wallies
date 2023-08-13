package com.oguzdogdu.wallies.presentation.setwallpaper

sealed class SetWallpaperState {
    data class SetWallpaper(val finallyPlace: String?) : SetWallpaperState()
}
