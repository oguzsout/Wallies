package com.oguzdogdu.wallieshd.presentation.collectionslistsbyid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.collection.GetCollectionsListByIdUseCase
import com.oguzdogdu.domain.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionsListsViewModel @Inject constructor(
    private val useCase: GetCollectionsListByIdUseCase,
) :
    ViewModel() {

    private val _getPhoto = MutableStateFlow<CollectionsListsState?>(null)
    val photo = _getPhoto.asStateFlow()

    fun handleUiEvents(event: CollectionListEvent) {
        when (event) {
            is CollectionListEvent.FetchCollectionList -> {
                getCollectionsLists(id = event.id)
            }
        }
    }

    private fun getCollectionsLists(id: String?) {
        viewModelScope.launch {
            useCase(id).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> _getPhoto.update { CollectionsListsState.Loading }

                    is Resource.Success -> _getPhoto.update {
                        CollectionsListsState.CollectionList(
                            collectionsLists = result.data
                        )
                    }

                    is Resource.Error -> _getPhoto.update {
                        CollectionsListsState.CollectionListError(
                            errorMessage = result.errorMessage
                        )
                    }
                }
            }
        }
    }
}
