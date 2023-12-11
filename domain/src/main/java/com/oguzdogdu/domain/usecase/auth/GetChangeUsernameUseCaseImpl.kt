package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.repository.Authenticator
import javax.inject.Inject

class GetChangeUsernameUseCaseImpl @Inject constructor(private val repository: Authenticator) :
    GetChangeUsernameUseCase {
    override suspend fun invoke(username: String?) = repository.changeUsername(name = username)
}