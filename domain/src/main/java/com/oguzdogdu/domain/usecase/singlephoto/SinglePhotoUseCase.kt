package com.oguzdogdu.domain.usecase.singlephoto

import com.oguzdogdu.domain.wrapper.Resource
import com.oguzdogdu.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class SinglePhotoUseCase @Inject constructor(
    private val repository: WallpaperRepository,
) {
    operator fun invoke(id: String?): Flow<Resource<com.oguzdogdu.domain.model.singlephoto.Photo>?> =
        flow {

            try {
                emit(Resource.Loading)
                id?.let { id ->
                    emit(Resource.Success(repository.getPhoto(id)))
                }
            } catch (e: IOException) {
                emit(Resource.Error(e.localizedMessage.orEmpty()))
            }
        }
}