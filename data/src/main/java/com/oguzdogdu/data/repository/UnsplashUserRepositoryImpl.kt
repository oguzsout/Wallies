package com.oguzdogdu.data.repository

import com.oguzdogdu.domain.model.userdetail.UserDetails
import com.oguzdogdu.domain.repository.UnsplashUserRepository
import com.oguzdogdu.network.model.userdetail.toDomain
import com.oguzdogdu.network.service.UnsplashUserService
import javax.inject.Inject

class UnsplashUserRepositoryImpl @Inject constructor(private val service: UnsplashUserService) :
    UnsplashUserRepository {
    override suspend fun getUserDetails(username: String?): UserDetails {
        return service.getUserDetailInfos(username = username).toDomain()
    }
}