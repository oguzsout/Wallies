package com.oguzdogdu.wallieshd.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.oguzdogdu.domain.usecase.search.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class SearchPhotoViewModel @Inject constructor(
    private val useCase: SearchUseCase
) :
    ViewModel() {

    private val _getSearchPhotos = MutableStateFlow<SearchPhotoState.ItemState?>(null)
    val getSearchPhotos = _getSearchPhotos.asStateFlow()

    fun handleUIEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.EnteredSearchQuery -> {
                getSearchPhotos(event.query)
            }
            else -> {}
        }
    }

    private fun getSearchPhotos(query: String) {
        viewModelScope.launch {
            useCase.invoke(query).cachedIn(viewModelScope).collectLatest { search ->
                _getSearchPhotos.update { SearchPhotoState.ItemState(search = search) }
            }
        }
    }
}
