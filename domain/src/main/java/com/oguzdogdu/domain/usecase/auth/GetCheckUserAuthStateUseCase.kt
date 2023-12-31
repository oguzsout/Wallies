package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface GetCheckUserAuthStateUseCase {
    suspend operator fun invoke(): Flow<Boolean>
}