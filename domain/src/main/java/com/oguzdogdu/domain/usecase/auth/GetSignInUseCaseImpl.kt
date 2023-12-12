package com.oguzdogdu.domain.usecase.auth

import com.google.firebase.auth.AuthResult
import com.oguzdogdu.domain.repository.Authenticator
import com.oguzdogdu.domain.wrapper.Resource
import com.oguzdogdu.domain.wrapper.toResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSignInUseCaseImpl @Inject constructor(
    private val repository: Authenticator,
) : GetSignInUseCase {
    override suspend fun invoke(userEmail: String?, password: String?): Flow<Resource<AuthResult>> {
        return flow {
            emit(repository.signIn(userEmail, password))
        }.toResource()
    }
}