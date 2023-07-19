package com.oguzdogdu.domain.usecase.userdetails

import com.oguzdogdu.domain.model.userdetail.UsersPhotos
import com.oguzdogdu.domain.repository.UnsplashUserRepository
import com.oguzdogdu.domain.wrapper.Resource
import com.oguzdogdu.domain.wrapper.toResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUnsplashUsersPhotosUseCase @Inject constructor(private val repository: UnsplashUserRepository) {
    suspend operator fun invoke(username:String?) : Flow<Resource<List<UsersPhotos>>> {
        return flow {
            emit(repository.getUsersPhotos(username = username))
        }.toResource()
    }
}