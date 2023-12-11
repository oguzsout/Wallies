package com.oguzdogdu.domain.usecase.auth

interface GetChangeProfilePhotoUseCase {
    suspend operator fun invoke(photo:String?)
}