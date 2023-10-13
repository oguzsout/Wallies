package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.repository.Authenticator
import javax.inject.Inject

class DeleteFavoriteToFirebaseUseCase @Inject constructor(private val repository: Authenticator) {
    suspend operator fun invoke(id: String?, favorite: String?) = repository.deleteFavorites(id, favorite)
}