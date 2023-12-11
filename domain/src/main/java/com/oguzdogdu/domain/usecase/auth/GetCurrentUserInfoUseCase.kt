package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.model.auth.User
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface GetCurrentUserInfoUseCase {
    suspend operator fun invoke(): Flow<Resource<User?>>
}