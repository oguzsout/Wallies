package com.oguzdogdu.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetPopularUseCase @Inject constructor(
    private val repository: WallpaperRepository
) {
    operator fun invoke(order:String?,page: Int?): Flow<Resource<List<PopularImage?>>> = flow {

        try {
            emit(Resource.Loading)
            emit(Resource.Success(repository.getPopularImages(order,page)))
        } catch (e: IOException) {
            emit(Resource.Error(e.localizedMessage.orEmpty()))
        }
    }
}