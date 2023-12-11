package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.repository.Authenticator
import javax.inject.Inject

class GetChangeProfilePhotoUseCaseImpl @Inject constructor(private val repository: Authenticator) :
    GetChangeProfilePhotoUseCase {
    override suspend fun invoke(photo: String?) = repository.changeProfilePhoto(photo = photo)
}