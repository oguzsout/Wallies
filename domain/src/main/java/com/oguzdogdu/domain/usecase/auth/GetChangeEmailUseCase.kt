package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface GetChangeEmailUseCase {
    suspend operator fun invoke(email:String?,password:String?) : Flow<Resource<Unit>>
}