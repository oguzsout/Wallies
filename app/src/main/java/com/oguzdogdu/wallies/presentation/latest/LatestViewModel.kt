package com.oguzdogdu.wallies.presentation.latest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.oguzdogdu.domain.usecase.latest.GetLatestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LatestViewModel @Inject constructor(private val useCase: GetLatestUseCase) : ViewModel() {
    private val _getLatest = MutableStateFlow(LatestState())
    val getLatest: StateFlow<LatestState>
        get() = _getLatest

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