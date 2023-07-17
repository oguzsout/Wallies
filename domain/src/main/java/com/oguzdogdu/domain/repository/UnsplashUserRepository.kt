package com.oguzdogdu.domain.repository

import com.oguzdogdu.domain.model.userdetail.UserDetails

interface UnsplashUserRepository {
suspend fun getUserDetails(username:String?): UserDetails
}