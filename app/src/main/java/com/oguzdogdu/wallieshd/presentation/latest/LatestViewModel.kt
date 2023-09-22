package com.oguzdogdu.wallieshd.presentation.latest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.oguzdogdu.domain.usecase.latest.GetLatestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LatestViewModel @Inject constructor(private val useCase: GetLatestUseCase) : ViewModel() {

    private val _getLatest = MutableStateFlow<LatestState.ItemState?>(null)
    val getLatest = _getLatest.asStateFlow()

    init {
        getLatestImages()
    }

    fun handleUIEvent(event: LatestScreenEvent) {
        when (event) {
            is LatestScreenEvent.FetchLatestData -> {
                getLatestImages()
            }
        }
    }

    private fun getLatestImages() {
        viewModelScope.launch {
            useCase().cachedIn(viewModelScope).collectLatest { latest ->
                latest.let {
                    _getLatest.update { LatestState.ItemState(latest = latest) }
                }
            }
        }
    }
}
