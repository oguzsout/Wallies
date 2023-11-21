package com.oguzdogdu.domain.usecase.singlephoto

import com.oguzdogdu.domain.model.singlephoto.Photo
import com.oguzdogdu.domain.repository.WallpaperRepository
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SinglePhotoUseCase @Inject constructor(
    private val repository: WallpaperRepository,
) {
    suspend operator fun invoke(id: String?): Flow<Resource<Photo?>> {
        return repository.getPhoto(id = id.orEmpty())
    }
}