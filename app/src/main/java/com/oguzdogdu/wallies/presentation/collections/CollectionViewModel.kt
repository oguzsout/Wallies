package com.oguzdogdu.wallies.presentation.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.oguzdogdu.data.common.Constants
import com.oguzdogdu.domain.Resource
import com.oguzdogdu.domain.repository.WallpaperRepository
import com.oguzdogdu.domain.usecase.collection.GetCollectionsUseCase
import com.oguzdogdu.wallies.presentation.popular.PopularState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(private val useCase: GetCollectionsUseCase,private val repository: WallpaperRepository) :
    ViewModel() {

    private val _getCollections = MutableStateFlow(CollectionState())
    val getCollections: StateFlow<CollectionState>
        get() = _getCollections

    init {
        getCollectionsList()
    }

    private fun getCollectionsList() {
        viewModelScope.launch {
            useCase().cachedIn(viewModelScope).collectLatest { result ->

                result.let {
                    _getCollections.value = CollectionState(collections = it)
                }
            }
        }
    }
}
