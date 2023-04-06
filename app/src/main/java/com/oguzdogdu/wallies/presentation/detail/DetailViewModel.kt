package com.oguzdogdu.wallies.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.data.common.Constants
import com.oguzdogdu.domain.Resource
import com.oguzdogdu.domain.model.singlephoto.Photo
import com.oguzdogdu.domain.usecase.singlephoto.SinglePhotoUseCase
import com.oguzdogdu.wallies.presentation.latest.LatestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val useCase: SinglePhotoUseCase) : ViewModel() {

    private val _getPhoto = MutableStateFlow(DetailState())
    val photo: StateFlow<DetailState>
        get() = _getPhoto


     fun getSinglePhoto(id: String) {
        useCase(id).onEach { result ->
            when (result) {
                is Resource.Loading -> _getPhoto.value = DetailState(isLoading = true)

                is Resource.Success -> {
                    result.data.let {
                        _getPhoto.value = DetailState(latest = it)
                    }
                }
                is Resource.Error -> {
                    _getPhoto.value = DetailState(error = result.errorMessage ?: "")
                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }
}