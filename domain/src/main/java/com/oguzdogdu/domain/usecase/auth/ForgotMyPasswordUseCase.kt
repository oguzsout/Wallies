package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.repository.Authenticator
import javax.inject.Inject

class ForgotMyPasswordUseCase @Inject constructor(private val repository : Authenticator){
    suspend operator fun invoke(email:String?) = repository.forgotMyPassword(email = email)
}