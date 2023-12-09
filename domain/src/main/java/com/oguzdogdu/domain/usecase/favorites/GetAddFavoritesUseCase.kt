package com.oguzdogdu.domain.usecase.favorites

import com.oguzdogdu.domain.model.favorites.FavoriteImages

interface GetAddFavoritesUseCase {
    suspend operator fun invoke(favoriteImage: FavoriteImages?)
}