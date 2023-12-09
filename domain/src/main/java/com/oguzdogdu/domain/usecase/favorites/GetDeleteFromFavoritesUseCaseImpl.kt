package com.oguzdogdu.domain.usecase.favorites

import com.oguzdogdu.domain.model.favorites.FavoriteImages
import com.oguzdogdu.domain.repository.WallpaperRepository
import javax.inject.Inject

class GetDeleteFromFavoritesUseCaseImpl
@Inject constructor(private val repository: WallpaperRepository) : GetDeleteFromFavoritesUseCase {
    override suspend fun invoke(favoriteImage: FavoriteImages?) =
        repository.deleteFavorites(favoriteImage)
}