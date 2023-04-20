package com.oguzdogdu.wallies.presentation.collectionslistsbyid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.Resource
import com.oguzdogdu.domain.usecase.collection.GetCollectionsListByIdUseCase
import com.oguzdogdu.wallies.presentation.collections.CollectionState
import com.oguzdogdu.wallies.presentation.detail.DetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
@HiltViewModel
class CollectionsListsViewModel @Inject constructor(private val useCase: GetCollectionsListByIdUseCase) : ViewModel() {

    private val _getPhoto = MutableStateFlow(CollectionsListsState())
    val photo: StateFlow<CollectionsListsState>
        get() = _getPhoto


    fun getCollectionsLists(id: String?) {
        useCase(id).onEach { result ->
            when (result) {
                is Resource.Loading -> _getPhoto.value = CollectionsListsState(isLoading = true)

                is Resource.Success -> {
                    result.data.let {
                        _getPhoto.value = CollectionsListsState(collectionsLists = it)
                    }
                }
                is Resource.Error -> {
                    _getPhoto.value = CollectionsListsState(error = result.errorMessage ?: "")
                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }
}