package com.oguzdogdu.wallieshd.presentation.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.oguzdogdu.domain.usecase.collection.GetCollectionByLikes
import com.oguzdogdu.domain.usecase.collection.GetCollectionByTitles
import com.oguzdogdu.domain.usecase.collection.GetCollectionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val useCase: GetCollectionsUseCase,
    private val getCollectionByTitles: GetCollectionByTitles,
    private val getCollectionByLikes: GetCollectionByLikes
) : ViewModel() {

    private val _getCollections = MutableStateFlow<CollectionState?>(null)
    val getCollections = _getCollections.asStateFlow()

    fun handleUIEvent(event: CollectionScreenEvent) {
        when (event) {
            is CollectionScreenEvent.FetchLatestData -> getCollectionsList()

            is CollectionScreenEvent.SortByTitles -> sortListByTitle()

            is CollectionScreenEvent.SortByLikes -> sortListByLikes()
        }
    }

    private fun getCollectionsList() {
        viewModelScope.launch {
            useCase().cachedIn(viewModelScope).collectLatest { collection ->
                collection.let {
                    _getCollections.update { CollectionState.ItemState(collections = collection) }
                }
            }
        }
    }

    private fun sortListByTitle() {
        viewModelScope.launch {
            getCollectionByTitles().cachedIn(viewModelScope).collectLatest { sortedPagingData ->
                _getCollections.update {
                    CollectionState.SortedByTitle(
                        collections = sortedPagingData
                    )
                }
            }
        }
    }

    private fun sortListByLikes() {
        viewModelScope.launch {
            getCollectionByLikes().cachedIn(viewModelScope).collectLatest { sortedPagingData ->
                _getCollections.update {
                    CollectionState.SortedByLikes(
                        collections = sortedPagingData
                    )
                }
            }
        }
    }
}
