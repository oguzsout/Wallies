package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.model.auth.User
import com.oguzdogdu.domain.repository.Authenticator
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentUserInfoUseCaseImpl @Inject constructor(private val repository: Authenticator) :
    GetCurrentUserInfoUseCase {
    override suspend fun invoke(): Flow<Resource<User?>> = repository.fetchUserInfos()
}