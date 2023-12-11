package com.oguzdogdu.domain.usecase.auth

interface GetForgotMyPasswordUseCase {
    suspend operator fun invoke(email:String?)
}