package com.oguzdogdu.wallies.presentation.search

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.oguzdogdu.domain.Resource
import com.oguzdogdu.domain.repository.WallpaperRepository
import com.oguzdogdu.domain.usecase.search.SearchUseCase
import com.oguzdogdu.wallies.presentation.popular.PopularState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchPhotoViewModel @Inject constructor(private val repository: WallpaperRepository,private val useCase: SearchUseCase) :
    ViewModel() {

    private val _getSearchPhotos = MutableStateFlow(SearchPhotoState())
    val getSearchPhotos: StateFlow<SearchPhotoState>
        get() = _getSearchPhotos


    fun getSearchPhotos(query: String) {
        viewModelScope.launch {
            useCase.invoke(query).onEach {
                _getSearchPhotos.value = SearchPhotoState(search = it)
            }
        }

//        viewModelScope.launch {
//            useCase.invoke(query).distinctUntilChanged().collectLatest { result ->
//                when(result){
//                    is Resource.Loading -> _getSearchPhotos.value = SearchPhotoState(isLoading = true)
//                    is Resource.Success -> {
//                        result.data.let {
//                            _getSearchPhotos.value = SearchPhotoState(search = it)
//                        }
//                    }
//                    is Resource.Error -> {
//                        _getSearchPhotos.value = SearchPhotoState(error = result.errorMessage ?: "")
//                    }
//            }
//            }
//        }

       viewModelScope.launch {
           repository.searchPhoto(query).collectLatest {  _getSearchPhotos.value = SearchPhotoState(search = it)  }

        }
    }
}