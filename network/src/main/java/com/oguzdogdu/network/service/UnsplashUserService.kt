package com.oguzdogdu.network.service

import com.oguzdogdu.network.model.collection.CollectionResponse
import com.oguzdogdu.network.model.maindto.Photo
import com.oguzdogdu.network.model.userdetail.UserDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UnsplashUserService {
    @GET("users/{username}")
    suspend fun getUserDetailInfos(
        @Path("username") username: String?,
    ): Response<UserDetailResponse>

    @GET("users/{username}/photos")
    suspend fun getUserPhotos(
        @Path("username") username: String?,
    ): Response<List<Photo>>

    @GET("users/{username}/collections")
    suspend fun getUserCollections(
        @Path("username") username: String?,
    ): Response<List<CollectionResponse>>
}