package com.oguzdogdu.wallieshd.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.oguzdogdu.domain.usecase.search.GetSearchPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SearchPhotoViewModel @Inject constructor(
    private val getSearchPhotosUseCase: GetSearchPhotosUseCase
) : ViewModel() {

    private val _getSearchPhotos = MutableStateFlow<SearchPhotoState.ItemState?>(null)
    val getSearchPhotos = _getSearchPhotos.asStateFlow()

    fun handleUIEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.EnteredSearchQuery -> {
                getSearchPhotos(event.query, event.language)
            }
        }
    }

    private fun getSearchPhotos(query: String?, language: String?) {
        viewModelScope.launch {
            getSearchPhotosUseCase.invoke(query, language).cachedIn(viewModelScope)
                .collectLatest { search ->
                    _getSearchPhotos.update { SearchPhotoState.ItemState(search = search) }
                }
        }
    }
}
