package com.oguzdogdu.domain.usecase.userdetails

import com.oguzdogdu.domain.model.userdetail.UserDetails
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface GetUnsplashUserDetailsUseCase {
    suspend operator fun invoke(username:String?) : Flow<Resource<UserDetails?>>
}