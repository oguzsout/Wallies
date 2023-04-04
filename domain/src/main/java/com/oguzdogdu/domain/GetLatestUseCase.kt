package com.oguzdogdu.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetLatestUseCase @Inject constructor(
    private val repository: WallpaperRepository
) {
    operator fun invoke(page: Int?): Flow<Resource<List<LatestImage?>>> = flow {

        try {
            emit(Resource.Loading)
            emit(Resource.Success(repository.getImagesByLatest(page)))
        } catch (e: IOException) {
            emit(Resource.Error(e.localizedMessage.orEmpty()))
        }
    }
}