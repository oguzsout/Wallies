package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.repository.Authenticator
import javax.inject.Inject

class GetForgotMyPasswordUseCaseImpl @Inject constructor(private val repository: Authenticator) :
    GetForgotMyPasswordUseCase {
    override suspend fun invoke(email: String?) = repository.forgotMyPassword(email = email)
}