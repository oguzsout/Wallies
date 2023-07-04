package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.model.auth.User
import com.oguzdogdu.domain.repository.Authenticator
import com.oguzdogdu.domain.wrapper.Resource
import com.oguzdogdu.domain.wrapper.toResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: Authenticator
) {
    operator fun invoke(user: User, password: String): Flow<Resource<User>> {
        return flow {
            emit(repository.signUp(user, password))
        }.toResource()
    }
}
