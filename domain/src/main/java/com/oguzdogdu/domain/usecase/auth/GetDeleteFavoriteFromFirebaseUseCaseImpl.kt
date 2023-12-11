package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.repository.Authenticator
import javax.inject.Inject

class GetDeleteFavoriteFromFirebaseUseCaseImpl
@Inject constructor(private val repository: Authenticator) : GetDeleteFavoriteFromFirebaseUseCase {
    override suspend fun invoke(id: String?, favorite: String?) =
        repository.deleteFavorites(id, favorite)
}