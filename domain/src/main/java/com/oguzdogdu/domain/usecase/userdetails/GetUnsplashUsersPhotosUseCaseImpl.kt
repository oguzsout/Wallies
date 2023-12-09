package com.oguzdogdu.domain.usecase.userdetails

import com.oguzdogdu.domain.model.userdetail.UsersPhotos
import com.oguzdogdu.domain.repository.UnsplashUserRepository
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUnsplashUsersPhotosUseCaseImpl @Inject constructor(private val repository: UnsplashUserRepository) :
    GetUnsplashUsersPhotosUseCase {
    override suspend fun invoke(username: String?): Flow<Resource<List<UsersPhotos>?>> =
        repository.getUsersPhotos(username = username)

}