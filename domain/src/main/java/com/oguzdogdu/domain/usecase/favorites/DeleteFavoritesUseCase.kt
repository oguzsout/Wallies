package com.oguzdogdu.domain.usecase.favorites

import com.oguzdogdu.domain.model.favorites.FavoriteImages
import com.oguzdogdu.domain.repository.WallpaperRepository
import javax.inject.Inject

class DeleteFavoritesUseCase @Inject constructor(private val repository: WallpaperRepository) {
    suspend operator fun invoke(favoriteImage: FavoriteImages) =
        repository.deleteFavorites(favoriteImage)
}