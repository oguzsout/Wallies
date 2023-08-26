package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.repository.Authenticator
import com.oguzdogdu.domain.wrapper.Resource
import com.oguzdogdu.domain.wrapper.toResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckGoogleSignInUseCase @Inject constructor(
    private val repository: Authenticator
) {
    operator fun invoke(): Flow<Resource<Boolean>> = flow {
        emit(repository.isUserAuthenticatedWithGoogle())
    }.toResource()
}