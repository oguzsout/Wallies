package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.repository.Authenticator
import javax.inject.Inject

class ChangeUserNameUseCase @Inject constructor(private val repository: Authenticator) {
    suspend operator fun invoke(name:String?) = repository.changeUsername(name = name)
}