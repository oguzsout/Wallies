package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.repository.Authenticator
import com.oguzdogdu.domain.wrapper.Resource
import com.oguzdogdu.domain.wrapper.toResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetChangeEmailUseCaseImpl @Inject constructor(private val repository: Authenticator): GetChangeEmailUseCase {
    override suspend fun invoke(email: String?, password: String?) : Flow<Resource<Unit>> {
       return flow {
           emit(repository.changeEmail(email = email, password = password))
       }.toResource()
    }
}