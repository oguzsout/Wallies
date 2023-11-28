package com.oguzdogdu.domain.repository

import com.oguzdogdu.domain.model.userdetail.UserCollections
import com.oguzdogdu.domain.model.userdetail.UserDetails
import com.oguzdogdu.domain.model.userdetail.UsersPhotos

interface UnsplashUserRepository {
suspend fun getUserDetails(username:String?): UserDetails
suspend fun getUsersPhotos(username: String?): List<UsersPhotos>
suspend fun getUsersCollections(username: String?): List<UserCollections>
}