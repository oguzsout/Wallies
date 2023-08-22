package com.oguzdogdu.wallies.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class FavoritesViewModel @Inject constructor(private val getFavoritesUseCase: GetFavoritesUseCase) :
    ViewModel() {

    private val _getFavorites = MutableStateFlow<FavoriteUiState?>(null)
    val favorites = _getFavorites.asStateFlow()

    fun handleUIEvent(event: FavoriteScreenEvent) {
        when (event) {
            is FavoriteScreenEvent.GetFavorites -> getFavoritesList()
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
