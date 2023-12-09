package com.oguzdogdu.domain.usecase.favorites

import com.oguzdogdu.domain.model.favorites.FavoriteImages

interface GetDeleteFromFavoritesUseCase {
    suspend operator fun invoke(favoriteImage: FavoriteImages?)
}