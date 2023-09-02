package com.oguzdogdu.data.repository

import com.oguzdogdu.domain.model.userdetail.UserDetails
import com.oguzdogdu.domain.model.userdetail.UsersPhotos
import com.oguzdogdu.domain.repository.UnsplashUserRepository
import com.oguzdogdu.network.model.maindto.toDomainUsersPhotos
import com.oguzdogdu.network.model.userdetail.toDomain
import com.oguzdogdu.network.service.UnsplashUserService
import javax.inject.Inject

class UnsplashUserRepositoryImpl @Inject constructor(private val service: UnsplashUserService) :
    UnsplashUserRepository {
    override suspend fun getUserDetails(username: String?): UserDetails {
        return service.getUserDetailInfos(username = username).body()?.toDomain()!!
    }

    override suspend fun getUsersPhotos(username: String?): List<UsersPhotos> {
        return service.getUserPhotos(username = username).body().orEmpty().map {
            it.toDomainUsersPhotos()
        }
    }
}