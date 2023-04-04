package com.oguzdogdu.wallies.presentation.latest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.data.common.Constants
import com.oguzdogdu.domain.GetLatestUseCase
import com.oguzdogdu.domain.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LatestViewModel @Inject constructor(private val useCase: GetLatestUseCase) : ViewModel() {
    private val _getLatest = MutableStateFlow(LatestState())
    val getLatest: StateFlow<LatestState>
        get() = _getLatest

    init {
        getLatestImages()
    }

    private fun getLatestImages() {
        useCase(Constants.PAGE).onEach { result ->
            when (result) {
                is Resource.Loading -> _getLatest.value = LatestState(isLoading = true)

                is Resource.Success -> {
                    result.data.let {
                        _getLatest.value = LatestState(latest = it)
                    }
                }
                is Resource.Error -> {
                    _getLatest.value = LatestState(error = result.errorMessage ?: "")
                }

            }
        }.launchIn(viewModelScope)
    }
}