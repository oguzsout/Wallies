package com.oguzdogdu.domain.usecase.userdetails

import com.oguzdogdu.domain.model.userdetail.UserDetails
import com.oguzdogdu.domain.repository.UnsplashUserRepository
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUnsplashUserDetailsUseCaseImpl @Inject constructor(private val repository: UnsplashUserRepository) :
    GetUnsplashUserDetailsUseCase {
    override suspend fun invoke(username: String?): Flow<Resource<UserDetails?>> =
        repository.getUserDetails(username = username)
}