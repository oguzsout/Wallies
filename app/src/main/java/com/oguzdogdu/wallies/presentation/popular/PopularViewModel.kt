package com.oguzdogdu.wallies.presentation.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.oguzdogdu.domain.usecase.popular.GetPopularUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(
    private val useCase: GetPopularUseCase,
) : ViewModel() {

    private val _getPopular = MutableStateFlow(PopularState())
    val getPopular = _getPopular.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        getPopularImages()
    }

     fun loadList() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(1000L)
            getPopularImages()
            _isLoading.value = false
        }
    }
    private fun getPopularImages() {
        viewModelScope.launch {
            val result = useCase().cachedIn(viewModelScope)
            _getPopular.value = PopularState(popular = result)
        }
    }
}