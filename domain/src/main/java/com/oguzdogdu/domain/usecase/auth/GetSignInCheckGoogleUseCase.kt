package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface GetSignInCheckGoogleUseCase {
    suspend operator fun invoke(): Flow<Boolean>
}