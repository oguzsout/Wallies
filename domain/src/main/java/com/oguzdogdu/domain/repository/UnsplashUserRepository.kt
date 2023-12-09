package com.oguzdogdu.domain.repository

import com.oguzdogdu.domain.model.userdetail.UserCollections
import com.oguzdogdu.domain.model.userdetail.UserDetails
import com.oguzdogdu.domain.model.userdetail.UsersPhotos
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface UnsplashUserRepository {
suspend fun getUserDetails(username:String?): UserDetails
suspend fun getUsersPhotos(username: String?): List<UsersPhotos>
suspend fun getUsersCollections(username: String?): Flow<Resource<List<UserCollections>?>>
}