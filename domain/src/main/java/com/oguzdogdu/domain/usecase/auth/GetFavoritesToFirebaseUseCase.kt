package com.oguzdogdu.domain.usecase.auth

interface GetFavoritesToFirebaseUseCase {
    suspend operator fun invoke(id:String?,favorite: String?)
}