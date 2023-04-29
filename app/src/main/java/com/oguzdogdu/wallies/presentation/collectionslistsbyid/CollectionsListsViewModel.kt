package com.oguzdogdu.wallies.presentation.collectionslistsbyid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.collection.GetCollectionsListByIdUseCase
import com.oguzdogdu.domain.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CollectionsListsViewModel @Inject constructor(private val useCase: GetCollectionsListByIdUseCase) :
    ViewModel() {

    private val _getPhoto = MutableStateFlow(CollectionsListsState())
    val photo = _getPhoto.asStateFlow()


    fun getCollectionsLists(id: String?) {
        viewModelScope.launch {
            useCase(id).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> _getPhoto.value = CollectionsListsState(isLoading = true)

                    is Resource.Success -> {
                        result.data.let {
                            _getPhoto.value = CollectionsListsState(collectionsLists = it)
                        }
                    }

                    is Resource.Error -> {
                        _getPhoto.value = CollectionsListsState(error = result.errorMessage)
                    }

                    else -> {}
                }
            }
        }
    }
}