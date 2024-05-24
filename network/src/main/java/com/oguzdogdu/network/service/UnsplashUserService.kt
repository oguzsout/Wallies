package com.oguzdogdu.network.service

import com.oguzdogdu.network.model.collection.CollectionResponse
import com.oguzdogdu.network.model.maindto.Photo
import com.oguzdogdu.network.model.searchdto.searchuser.SearchUsersResponse
import com.oguzdogdu.network.model.userdetail.UserDetailResponse

interface UnsplashUserService {
    suspend fun getUserDetailInfos(
        username: String?,
    ): UserDetailResponse

    suspend fun getUserPhotos(
       username: String?,
    ): List<Photo>

    suspend fun getUserCollections(
         username: String?,
    ): List<CollectionResponse>

    suspend fun getSearchUserData(
       page: Int?,
        query: String?,
    ): SearchUsersResponse
}