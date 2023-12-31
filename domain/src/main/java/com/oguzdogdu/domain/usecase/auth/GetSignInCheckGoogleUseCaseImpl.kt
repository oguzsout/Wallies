package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.repository.Authenticator
import com.oguzdogdu.domain.wrapper.Resource
import com.oguzdogdu.domain.wrapper.toResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSignInCheckGoogleUseCaseImpl @Inject constructor(
    private val repository: Authenticator,
) : GetSignInCheckGoogleUseCase {
    override suspend fun invoke(): Flow<Boolean> = flow {
        emit(repository.isUserAuthenticatedWithGoogle())
    }
}