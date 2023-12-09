package com.oguzdogdu.domain.usecase.singlephoto

import com.oguzdogdu.domain.model.singlephoto.Photo
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface GetPhotoDetailUseCase {
    suspend operator fun invoke(id: String?): Flow<Resource<Photo?>>
}