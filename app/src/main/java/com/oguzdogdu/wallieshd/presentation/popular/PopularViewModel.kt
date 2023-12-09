package com.oguzdogdu.wallieshd.presentation.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.oguzdogdu.domain.usecase.popular.GetPopularUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PopularViewModel @Inject constructor(
    private val getPopularUseCase: GetPopularUseCase
) : ViewModel() {

    private val _getPopular = MutableStateFlow<PopularState.ItemState?>(null)
    val getPopular = _getPopular.asStateFlow()

    init {
        getPopularImages()
    }

    fun handleUIEvent(event: PopularScreenEvent) {
        when (event) {
            PopularScreenEvent.FetchPopularData -> {
                getPopularImages()
            }
        }
    }

    private fun getPopularImages() {
        viewModelScope.launch {
            getPopularUseCase().cachedIn(viewModelScope).collectLatest { popular ->
                _getPopular.update { PopularState.ItemState(popular = popular) }
            }
        }
    }
}
