package com.oguzdogdu.wallieshd.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.oguzdogdu.domain.model.search.SearchPhoto
import com.oguzdogdu.domain.model.search.searchuser.SearchUser
import com.oguzdogdu.domain.repository.DataStore
import com.oguzdogdu.domain.usecase.search.GetSearchPhotosUseCase
import com.oguzdogdu.domain.usecase.search.searchuser.GetSearchUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchPhotosUseCase: GetSearchPhotosUseCase,
    private val getSearchUserUseCase: GetSearchUserUseCase,
    private val dataStore: DataStore
) : ViewModel() {

    private val _getSearchContents = MutableStateFlow<SearchState?>(null)
    val getSearchContents = _getSearchContents.asStateFlow()

    var appLanguage = MutableStateFlow("")
        private set

    fun handleUIEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.EnteredSearchQuery -> {
                asyncQuery(query = event.query, language = event.language)
            }

            is SearchEvent.GetAppLanguageValue -> getLanguageValue()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun asyncQuery(query: String?, language: String?) {
        viewModelScope.launch {
            getSearchPhotos(query, language)
                .flatMapConcat { photo ->
                    _getSearchContents.update { SearchState.PhotoState(searchPhoto = photo) }
                    getSearchUsers(query)
                }
                .collect { users ->
                    _getSearchContents.update { SearchState.UserState(searchUser = users) }
                }
        }
    }

    private suspend fun getSearchPhotos(
        query: String?,
        language: String?
    ): Flow<PagingData<SearchPhoto>> {
        return getSearchPhotosUseCase.invoke(query, language).cachedIn(viewModelScope)
    }

    private suspend fun getSearchUsers(query: String?): Flow<PagingData<SearchUser>> {
        return getSearchUserUseCase.invoke(query).cachedIn(viewModelScope)
    }

    private fun getLanguageValue() {
        viewModelScope.launch {
            val language = dataStore.getLanguageStrings(key = "language").single()
            appLanguage.value = language ?: "en"
        }
    }
}
