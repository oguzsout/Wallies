package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.model.auth.User
import com.oguzdogdu.domain.repository.Authenticator
import com.oguzdogdu.domain.wrapper.Resource
import com.oguzdogdu.domain.wrapper.toResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrentUserDatasUseCase @Inject constructor(private val repository: Authenticator) {
     suspend operator fun invoke(): Flow<Resource<User>> = flow {
        emit(repository.fetchUserInfos())
    }.toResource()
}