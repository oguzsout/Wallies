package com.oguzdogdu.domain.usecase.favorites

import com.oguzdogdu.domain.model.favorites.FavoriteImages
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface GetImageFromFavoritesUseCase {
    suspend operator fun invoke(): Flow<Resource<List<FavoriteImages>?>>
}