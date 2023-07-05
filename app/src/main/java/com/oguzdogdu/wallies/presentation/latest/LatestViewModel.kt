package com.oguzdogdu.wallies.presentation.latest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.oguzdogdu.domain.usecase.latest.GetLatestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class LatestViewModel @Inject constructor(private val useCase: GetLatestUseCase) : ViewModel() {

    private val _getLatest = MutableStateFlow(LatestState())
    val getLatest = _getLatest.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        getLatestImages()
    }

    fun loadList() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(1000L)
            getLatestImages()
            _isLoading.value = false
        }
    }

    fun getLatestImages() {
        viewModelScope.launch {
            useCase().cachedIn(viewModelScope).collectLatest { latest ->
                latest.let {
                    _getLatest.value = LatestState(latest = it)
                }
            }
        }
    }
}
