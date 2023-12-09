package com.oguzdogdu.network.service

import com.oguzdogdu.network.model.collection.CollectionResponse
import com.oguzdogdu.network.model.maindto.Photo
import com.oguzdogdu.network.model.maindto.UnsplashResponseItem
import com.oguzdogdu.network.model.searchdto.SearchResponseItem
import com.oguzdogdu.network.model.topics.TopicsResponseItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WallpaperService {

    @GET("photos")
    suspend fun getImagesByOrders(
        @Query("per_page") perPage: Int?,
        @Query("page") page: Int?,
        @Query("order_by") order: String?,
    ): Response<List<UnsplashResponseItem>>

    @GET("photos/{id}")
    suspend fun getPhoto(
        @Path("id") id: String?,
    ): Response<UnsplashResponseItem>

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("query") query: String,
        @Query("lang") language: String?,
    ): Response<SearchResponseItem>

    @GET("collections")
    suspend fun getCollections(
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?
    ): Response<List<CollectionResponse>>

    @GET("collections/{id}/photos")
    suspend fun getCollectionsListById(
        @Path("id") id: String?,
    ): Response<List<Photo>>

    @GET("topics")
    suspend fun getTopics(@Query("per_page") perPage: Int? = 6): Response<List<TopicsResponseItem>>
}