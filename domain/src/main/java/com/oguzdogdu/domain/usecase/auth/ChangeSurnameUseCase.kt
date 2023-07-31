package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.repository.Authenticator
import javax.inject.Inject

class ChangeSurnameUseCase @Inject constructor(private val repository: Authenticator) {
    suspend operator fun invoke(surname:String?) = repository.changeSurname(surname = surname)
}