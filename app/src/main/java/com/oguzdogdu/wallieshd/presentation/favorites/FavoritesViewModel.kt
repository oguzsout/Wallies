package com.oguzdogdu.wallieshd.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.model.favorites.FavoriteImages
import com.oguzdogdu.domain.usecase.auth.CheckUserAuthenticatedUseCase
import com.oguzdogdu.domain.usecase.auth.GetCurrentUserDatasUseCase
import com.oguzdogdu.domain.usecase.favorites.GetFavoritesUseCase
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
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val checkUserAuthenticatedUseCase: CheckUserAuthenticatedUseCase,
    private val getCurrentUserDatasUseCase: GetCurrentUserDatasUseCase
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
            checkUserAuthenticatedUseCase.invoke().collectLatest { status ->
                when (status) {
                    is Resource.Success -> {
                        when (status.data) {
                            true -> getFavoritesFromFirebase()
                            false -> getFavoritesList()
                        }
                    }

                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                }
            }
        }
    }

    private fun getFavoritesFromFirebase() {
        viewModelScope.launch {
            getCurrentUserDatasUseCase.invoke().collectLatest { userDatas ->
                when (userDatas) {
                    is Resource.Success -> {
                        val remoteList = userDatas.data.favorites.map { favoriteMap ->
                            val id = favoriteMap["id"] ?: ""
                            val url = favoriteMap["favorite"] ?: ""
                            FavoriteImages(id = id, url = url)
                        }
                        _getFavorites.update {
                            FavoriteUiState.FavoritesFromFirebase(remoteList)
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
            getFavoritesUseCase.invoke().collectLatest { result ->
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
