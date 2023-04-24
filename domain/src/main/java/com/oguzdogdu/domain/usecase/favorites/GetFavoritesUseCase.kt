package com.oguzdogdu.domain.usecase.favorites

import com.oguzdogdu.domain.model.favorites.FavoriteImages
import com.oguzdogdu.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(private val repository: WallpaperRepository) {

    suspend operator fun invoke(): Flow<List<FavoriteImages>> = flow {
        emit(repository.getFavorites())
    }

}