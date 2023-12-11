package com.oguzdogdu.domain.usecase.auth

import com.google.firebase.auth.AuthResult
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface GetSignInWithGoogleUseCase {
    suspend operator fun invoke(idToken: String?): Flow<Resource<AuthResult>>
}