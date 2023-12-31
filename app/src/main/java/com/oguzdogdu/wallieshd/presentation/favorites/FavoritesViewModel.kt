package com.oguzdogdu.wallieshd.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.model.favorites.FavoriteImages
import com.oguzdogdu.domain.usecase.auth.GetCheckUserAuthStateUseCase
import com.oguzdogdu.domain.usecase.auth.GetCurrentUserInfoUseCase
import com.oguzdogdu.domain.usecase.favorites.GetImageFromFavoritesUseCase
import com.oguzdogdu.domain.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getImageFromFavoritesUseCase: GetImageFromFavoritesUseCase,
    private val getCheckUserAuthStateUseCase: GetCheckUserAuthStateUseCase,
    private val getCurrentUserInfoUseCase: GetCurrentUserInfoUseCase
) :
    ViewModel() {

    private val _getFavorites = MutableStateFlow<FavoriteUiState?>(null)
    val favorites = _getFavorites.asStateFlow()

    fun handleUIEvent(event: FavoriteScreenEvent) {
        when (event) {
            is FavoriteScreenEvent.GetFavorites -> fetchImagesToFavorites()
        }
    }

    private fun fetchImagesToFavorites() {
        viewModelScope.launch {
            getCheckUserAuthStateUseCase.invoke().collectLatest { status ->
                when (status) {
                    true -> getFavoritesFromFirebase()
                    false -> getFavoritesList()
                }
            }
        }
    }

    private fun getFavoritesFromFirebase() {
        viewModelScope.launch {
            getCurrentUserInfoUseCase.invoke().collectLatest { userDatas ->
                when (userDatas) {
                    is Resource.Success -> {
                        val remoteList = userDatas.data?.favorites?.map { favoriteMap ->
                            val id = favoriteMap?.get("id") ?: ""
                            val url = favoriteMap?.get("favorite") ?: ""
                            FavoriteImages(id = id, url = url)
                        }
                        _getFavorites.update {
                            remoteList?.let { list -> FavoriteUiState.FavoritesFromFirebase(list) }
                        }
                    }
                    is Resource.Error -> {}
                    is Resource.Loading -> {
                        _getFavorites.update {
                            FavoriteUiState.Loading
                        }
                    }
                }
            }
        }
    }

    private fun getFavoritesList() {
        viewModelScope.launch(Dispatchers.IO) {
            getImageFromFavoritesUseCase.invoke().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> _getFavorites.update { FavoriteUiState.Loading }
                    is Resource.Error -> _getFavorites.update {
                        FavoriteUiState.FavoriteError(
                            errorMessage = result.errorMessage
                        )
                    }

                    is Resource.Success -> _getFavorites.update {
                        FavoriteUiState.Favorites(
                            favorites = result.data
                        )
                    }

                    else -> {}
                }
            }
        }
    }
}
