package com.oguzdogdu.wallies.presentation.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.oguzdogdu.domain.usecase.collection.GetCollectionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class CollectionViewModel @Inject constructor(private val useCase: GetCollectionsUseCase) :
    ViewModel() {

    private val _getCollections = MutableStateFlow(CollectionState())
    val getCollections = _getCollections.asStateFlow()

    fun getCollectionsList() {
        viewModelScope.launch {
            useCase().cachedIn(viewModelScope).collectLatest { collection ->
                collection.let {
                    _getCollections.value = CollectionState(collections = it)
                }
            }
        }
    }
}
