package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.model.auth.User
import com.oguzdogdu.domain.repository.Authenticator
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSignUpUseCaseImpl @Inject constructor(
    private val repository: Authenticator
): GetSignUpUseCase {
    override suspend fun invoke(user: User?, password: String?): Flow<Resource<User?>> = repository.signUp(user, password)
}