package com.oguzdogdu.network.service

import com.oguzdogdu.network.model.maindto.Photo
import com.oguzdogdu.network.model.userdetail.UserDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface UnsplashUserService {
    @GET("users/{username}")
    suspend fun getUserDetailInfos(
        @Path("username") id: String?,
    ): UserDetailResponse
}