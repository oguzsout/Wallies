package com.oguzdogdu.domain.usecase.userdetails

import com.oguzdogdu.domain.model.userdetail.UsersPhotos
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface GetUnsplashUsersPhotosUseCase {
    suspend operator fun invoke(username:String?) : Flow<Resource<List<UsersPhotos>?>>
}