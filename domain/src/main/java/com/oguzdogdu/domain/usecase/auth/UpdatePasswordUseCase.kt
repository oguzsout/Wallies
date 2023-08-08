package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.repository.Authenticator
import com.oguzdogdu.domain.wrapper.toResource
import javax.inject.Inject

class UpdatePasswordUseCase @Inject constructor(private val repository:Authenticator) {
    suspend operator fun invoke(password:String?) = repository.updatePassword(password = password).toResource()
}