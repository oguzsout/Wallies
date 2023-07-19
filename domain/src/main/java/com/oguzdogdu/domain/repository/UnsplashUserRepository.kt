package com.oguzdogdu.domain.repository

import com.oguzdogdu.domain.model.userdetail.UserDetails
import com.oguzdogdu.domain.model.userdetail.UsersPhotos

interface UnsplashUserRepository {
suspend fun getUserDetails(username:String?): UserDetails
suspend fun getUsersPhotos(username: String?): List<UsersPhotos>
}