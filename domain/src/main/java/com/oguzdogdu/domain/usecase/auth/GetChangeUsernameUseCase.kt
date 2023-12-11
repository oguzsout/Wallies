package com.oguzdogdu.domain.usecase.auth

interface GetChangeUsernameUseCase {
    suspend operator fun invoke(username:String?)
}