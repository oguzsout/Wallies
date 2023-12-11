package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.repository.Authenticator
import javax.inject.Inject

class GetChangeSurnameUseCaseImpl @Inject constructor(private val repository: Authenticator) :
    GetChangeSurnameUseCase {
    override suspend fun invoke(surname: String?) = repository.changeSurname(surname = surname)
}