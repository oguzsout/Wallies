package com.oguzdogdu.domain.usecase.auth

interface GetDeleteFavoriteFromFirebaseUseCase {
    suspend operator fun invoke(id: String?, favorite: String?)
}