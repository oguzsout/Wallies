package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.model.auth.User
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface GetSignUpUseCase {
    suspend operator fun invoke(user: User?, password: String?): Flow<Resource<User?>>
}