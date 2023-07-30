package com.oguzdogdu.wallies.presentation.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.oguzdogdu.domain.usecase.popular.GetPopularUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class PopularViewModel @Inject constructor(
    private val useCase: GetPopularUseCase
) : ViewModel() {

    private val _getPopular = MutableStateFlow(PopularState())
    val getPopular = _getPopular.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun getPopularImages() {
        viewModelScope.launch {
            useCase().cachedIn(viewModelScope).collectLatest { popular ->
                popular.let {
                    _getPopular.value = PopularState(popular = it)
                }
            }
        }
    }
}
