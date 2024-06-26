package com.oguzdogdu.domain.usecase.favorites

import com.oguzdogdu.domain.model.favorites.FavoriteImages
import com.oguzdogdu.domain.repository.WallpaperRepository
import com.oguzdogdu.domain.wrapper.Resource
import com.oguzdogdu.domain.wrapper.toResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetImageFromFavoritesUseCaseImpl @Inject constructor(
    private val repository: WallpaperRepository,
) : GetImageFromFavoritesUseCase {
    override suspend fun invoke(): Flow<Resource<List<FavoriteImages>?>> =
        repository.getFavorites().toResource()
}