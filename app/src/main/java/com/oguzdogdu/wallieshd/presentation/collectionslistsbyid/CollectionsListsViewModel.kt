package com.oguzdogdu.wallieshd.presentation.collectionslistsbyid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.collection.GetCollectionListByIdUseCase
import com.oguzdogdu.domain.wrapper.onFailure
import com.oguzdogdu.domain.wrapper.onLoading
import com.oguzdogdu.domain.wrapper.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CollectionsListsViewModel @Inject constructor(
    private val getCollectionListByIdUseCase: GetCollectionListByIdUseCase
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
            getCollectionListByIdUseCase(id).collectLatest { result ->
                result.onLoading {
                    _getPhoto.update { CollectionsListsState.Loading }
                }

                result.onSuccess { list ->
                    _getPhoto.update {
                        CollectionsListsState.CollectionList(
                            collectionsLists = list
                        )
                    }
                }

                result.onFailure { error ->
                    _getPhoto.update {
                        CollectionsListsState.CollectionListError(
                            errorMessage = error
                        )
                    }
                }
            }
        }
    }
}
