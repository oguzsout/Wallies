package com.oguzdogdu.wallieshd.presentation.favorites

import com.oguzdogdu.domain.model.favorites.FavoriteImages

sealed class FavoriteUiState {
    object Loading : FavoriteUiState()
    data class FavoriteError(val errorMessage: String?) : FavoriteUiState()
    data class Favorites(val favorites: List<FavoriteImages?>) : FavoriteUiState()
}
