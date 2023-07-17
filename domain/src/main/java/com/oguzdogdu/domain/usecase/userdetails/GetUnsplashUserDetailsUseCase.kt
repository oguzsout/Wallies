package com.oguzdogdu.domain.usecase.userdetails

import com.oguzdogdu.domain.model.userdetail.UserDetails
import com.oguzdogdu.domain.repository.UnsplashUserRepository
import com.oguzdogdu.domain.wrapper.Resource
import com.oguzdogdu.domain.wrapper.toResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUnsplashUserDetailsUseCase @Inject constructor(private val repository: UnsplashUserRepository) {
    suspend operator fun invoke(username:String?) : Flow<Resource<UserDetails>> {
        return flow {
            emit(repository.getUserDetails(username = username))
        }.toResource()
    }
}