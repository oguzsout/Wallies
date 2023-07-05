package com.oguzdogdu.wallies.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.oguzdogdu.domain.usecase.search.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class SearchPhotoViewModel @Inject constructor(
    private val useCase: SearchUseCase
) :
    ViewModel() {

    private val _getSearchPhotos = MutableStateFlow(SearchPhotoState())
    val getSearchPhotos = _getSearchPhotos.asStateFlow()

    private val _eventChannel = Channel<SearchEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()

    fun handleUIEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.EnteredSearchQuery -> {
                _getSearchPhotos.value = _getSearchPhotos.value.copy(query = event.query)
                getSearchPhotos(event.query)
            }
            else -> {}
        }
    }

    private fun getSearchPhotos(query: String) {
        viewModelScope.launch {
            useCase.invoke(query).cachedIn(viewModelScope).collectLatest {
                _getSearchPhotos.value = SearchPhotoState(search = it)
                _eventChannel.send(SearchEvent.Success)
            }
        }
    }
}
