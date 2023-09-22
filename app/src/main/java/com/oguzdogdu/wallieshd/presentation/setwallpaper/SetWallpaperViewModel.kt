package com.oguzdogdu.wallieshd.presentation.setwallpaper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SetWallpaperViewModel @Inject constructor() : ViewModel() {

    private val _wallpaperState = MutableStateFlow<SetWallpaperState.SetWallpaper?>(null)
    val wallpaperState = _wallpaperState.asStateFlow()

    fun handleUIEvent(event: SetWallpaperEvent) {
        when (event) {
            is SetWallpaperEvent.AdjustWallpaper -> {
                setWallpaperToAnyPlace(event.place)
            }
        }
    }

    private fun setWallpaperToAnyPlace(place: String?) {
        viewModelScope.launch {
            _wallpaperState.update { SetWallpaperState.SetWallpaper(finallyPlace = place) }
        }
    }
}
