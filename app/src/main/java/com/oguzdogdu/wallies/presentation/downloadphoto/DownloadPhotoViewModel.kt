package com.oguzdogdu.wallies.presentation.downloadphoto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DownloadPhotoViewModel @Inject constructor() : ViewModel() {

    private val _downloadPhotoState = MutableStateFlow<DownloadPhotoState.PhotoQuality?>(null)
    val downloadPhotoState = _downloadPhotoState.asStateFlow()

    fun handleUIEvent(event: DownloadPhotoEvent) {
        when (event) {
            is DownloadPhotoEvent.ClickedRaw -> {
                getPhotoQuality(DownloadPhotoFragment.RAW)
            }
            is DownloadPhotoEvent.ClickedFull -> {
                getPhotoQuality(DownloadPhotoFragment.FULL)
            }

            is DownloadPhotoEvent.ClickedMedium -> {
                getPhotoQuality(DownloadPhotoFragment.MEDIUM)
            }

            is DownloadPhotoEvent.ClickedLow -> {
                getPhotoQuality(DownloadPhotoFragment.LOW)
            }
        }
    }

    private fun getPhotoQuality(quality: String?) {
        viewModelScope.launch {
            _downloadPhotoState.update { DownloadPhotoState.PhotoQuality(quality = quality) }
        }
    }
}
