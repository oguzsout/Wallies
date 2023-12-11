package com.oguzdogdu.domain.usecase.auth

import com.google.android.gms.tasks.Task
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface GetUpdatePasswordUseCase {
    suspend operator fun invoke(password: String?): Flow<Resource<Task<Void>?>>
}