package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.repository.Authenticator
import javax.inject.Inject

class GetFavoritesToFirebaseUseCaseImpl @Inject constructor(private val repository: Authenticator) :
    GetFavoritesToFirebaseUseCase {
    override suspend fun invoke(id: String?, favorite: String?) =
        repository.addFavorites(id, favorite)
}