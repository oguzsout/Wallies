package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.repository.Authenticator
import javax.inject.Inject

class GetSignOutUseCaseImpl @Inject constructor(
    private val repository: Authenticator,
) : GetSignOutUseCase {
    override suspend fun invoke() = repository.signOut()
}