package com.oguzdogdu.domain.usecase.auth

import com.google.firebase.auth.AuthResult
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface GetSignInUseCase {
    suspend operator fun invoke(userEmail: String?, password: String?): Flow<Resource<AuthResult>>
}