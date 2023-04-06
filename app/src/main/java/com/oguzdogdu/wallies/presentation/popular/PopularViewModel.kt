package com.oguzdogdu.wallies.presentation.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.data.common.Constants.PAGE
import com.oguzdogdu.domain.Resource
import com.oguzdogdu.domain.usecase.popular.GetPopularUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(private val useCase: GetPopularUseCase) : ViewModel() {
    private val _getPopular = MutableStateFlow(PopularState())
    val getPopular: StateFlow<PopularState>
        get() = _getPopular

    init {
        getPopularImages()
    }

     fun getPopularImages() {
        useCase(PAGE).onEach { result ->
            when (result) {
                is Resource.Loading -> _getPopular.value = PopularState(isLoading = true)

                is Resource.Success -> {
                    result.data.let {
                        _getPopular.value = PopularState(popular = it)
                    }
                }
                is Resource.Error -> {
                    _getPopular.value = PopularState(error = result.errorMessage ?: "")
                }

            }
        }.launchIn(viewModelScope)
    }
}