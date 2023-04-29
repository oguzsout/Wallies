package com.oguzdogdu.wallies.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.oguzdogdu.domain.usecase.search.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchPhotoViewModel @Inject constructor(
    private val useCase: SearchUseCase,
) :
    ViewModel() {

    private val _getSearchPhotos = MutableStateFlow(SearchPhotoState())
    val getSearchPhotos = _getSearchPhotos.asStateFlow()


    fun getSearchPhotos(query: String) {
        viewModelScope.launch {
            useCase.invoke(query).cachedIn(viewModelScope).collectLatest {
                _getSearchPhotos.value = SearchPhotoState(search = it)
            }
        }
    }
}