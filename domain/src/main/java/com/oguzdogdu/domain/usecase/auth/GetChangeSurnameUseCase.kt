package com.oguzdogdu.domain.usecase.auth

interface GetChangeSurnameUseCase {
    suspend operator fun invoke(surname:String?)
}