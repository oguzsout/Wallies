package com.oguzdogdu.domain.usecase.singlephoto

import com.oguzdogdu.domain.model.singlephoto.Photo
import com.oguzdogdu.domain.repository.WallpaperRepository
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotoDetailUseCaseImpl @Inject constructor(
    private val repository: WallpaperRepository,
) : GetPhotoDetailUseCase {
    override suspend fun invoke(id: String?): Flow<Resource<Photo?>> =
        repository.getPhoto(id = id.orEmpty())
}