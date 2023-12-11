package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.repository.Authenticator
import com.oguzdogdu.domain.wrapper.Resource
import com.oguzdogdu.domain.wrapper.toResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCheckUserAuthStateUseCaseImpl @Inject constructor(
    private val repository: Authenticator,
) : GetCheckUserAuthStateUseCase {
    override suspend fun invoke(): Flow<Resource<Boolean>> = flow {
        emit(repository.isUserAuthenticatedInFirebase())
    }.toResource()
}