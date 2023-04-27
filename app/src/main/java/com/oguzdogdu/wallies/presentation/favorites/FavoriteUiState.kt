package com.oguzdogdu.wallies.presentation.favorites

import com.oguzdogdu.domain.model.favorites.FavoriteImages

data class FavoriteUiState(
    val isLoading: Boolean = false,
    val favorites: List<FavoriteImages?> = emptyList(),
    val error: String = ""
)
