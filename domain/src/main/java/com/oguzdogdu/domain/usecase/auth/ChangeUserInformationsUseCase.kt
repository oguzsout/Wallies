package com.oguzdogdu.domain.usecase.auth

import com.oguzdogdu.domain.model.auth.User
import com.oguzdogdu.domain.repository.Authenticator
import com.oguzdogdu.domain.wrapper.Resource
import com.oguzdogdu.domain.wrapper.toResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChangeUserInformationsUseCase @Inject constructor(private val repository: Authenticator) {
    operator fun invoke(name: String?, surname:String?, email:String?, image:String?): Flow<Resource<User>> {
        return flow {
            emit(repository.changeUserInfos(name, surname, email, image))
        }.toResource()
    }
}