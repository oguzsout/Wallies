package com.oguzdogdu.domain.usecase.auth

import com.google.firebase.auth.AuthResult
import com.oguzdogdu.domain.repository.Authenticator
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSignInWithGoogleUseCaseImpl @Inject constructor(
    private val repository: Authenticator,
) : GetSignInWithGoogleUseCase {
    override suspend fun invoke(idToken: String?): Flow<Resource<AuthResult>> =
        repository.signInWithGoogle(idToken = idToken)
}