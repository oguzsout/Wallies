package com.oguzdogdu.domain.usecase.favorites

import com.oguzdogdu.domain.model.collection.CollectionList
import com.oguzdogdu.domain.model.favorites.FavoriteImages
import com.oguzdogdu.domain.repository.WallpaperRepository
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(private val repository: WallpaperRepository) {
   suspend operator fun invoke(): Flow<Resource<List<FavoriteImages>>> = flow {

        try {
            emit(Resource.Loading)
            emit(Resource.Success(repository.getFavorites()))
        } catch (e: IOException) {
            emit(Resource.Error(e.localizedMessage.orEmpty()))
        }
    }
}