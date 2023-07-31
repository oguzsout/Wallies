package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.repository.Authenticator
import javax.inject.Inject

class ChangeUserProfilePhotoUseCase @Inject constructor(private val repository: Authenticator) {
    suspend operator fun invoke(photo:String?) = repository.changeProfilePhoto(photo = photo)
}