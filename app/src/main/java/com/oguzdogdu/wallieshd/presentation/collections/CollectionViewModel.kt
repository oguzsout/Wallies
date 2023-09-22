package com.oguzdogdu.wallieshd.presentation.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.oguzdogdu.domain.usecase.collection.GetCollectionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CollectionViewModel @Inject constructor(private val useCase: GetCollectionsUseCase) :
    ViewModel() {

    private val _getCollections = MutableStateFlow<CollectionState.ItemState?>(null)
    val getCollections = _getCollections.asStateFlow()

    init {
        getCollectionsList()
    }

    fun handleUIEvent(event: CollectionScreenEvent) {
        when (event) {
            is CollectionScreenEvent.FetchLatestData -> {
                getCollectionsList()
            }
        }
    }

    private fun getCollectionsList() {
        viewModelScope.launch {
            useCase().cachedIn(viewModelScope).collectLatest { collection ->
                _getCollections.update { CollectionState.ItemState(collections = collection) }
            }
        }
    }
}
