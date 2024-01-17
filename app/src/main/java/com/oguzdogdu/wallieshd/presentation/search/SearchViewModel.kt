package com.oguzdogdu.wallieshd.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.oguzdogdu.domain.repository.DataStore
import com.oguzdogdu.domain.usecase.search.GetSearchPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchPhotosUseCase: GetSearchPhotosUseCase,
    private val dataStore: DataStore
) : ViewModel() {

    private val _getSearchPhotos = MutableStateFlow<SearchState?>(null)
    val getSearchPhotos = _getSearchPhotos.asStateFlow()

    var appLanguage = MutableStateFlow("")
        private set

    fun handleUIEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.EnteredSearchQuery -> {
                getSearchPhotos(event.query, event.language)
            }

            is SearchEvent.GetAppLanguageValue -> getLanguageValue()
        }
    }

    private fun getSearchPhotos(query: String?, language: String?) {
        viewModelScope.launch {
            getSearchPhotosUseCase.invoke(query, language).cachedIn(viewModelScope)
                .collectLatest { search ->
                    _getSearchPhotos.update { SearchState.PhotoState(searchPhoto = search) }
                }
        }
    }
    private fun getLanguageValue() {
        viewModelScope.launch {
            val language = dataStore.getLanguageStrings(key = "language").single()
            appLanguage.value = language ?: "en"
        }
    }
}
