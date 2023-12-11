package com.oguzdogdu.domain.usecase.auth

import com.google.android.gms.tasks.Task
import com.oguzdogdu.domain.repository.Authenticator
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUpdatePasswordUseCaseImpl @Inject constructor(private val repository: Authenticator) :
    GetUpdatePasswordUseCase {
    override suspend fun invoke(password: String?): Flow<Resource<Task<Void>?>> =
        repository.updatePassword(password = password)
}