package com.oguzdogdu.wallies.presentation.detail

import com.oguzdogdu.domain.model.favorites.FavoriteImages

data class FavoriteState(
    val isLoading: Boolean = false,
    val favorites: List<FavoriteImages?> = emptyList(),
    val error: String = ""
)
