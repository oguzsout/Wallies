package com.oguzdogdu.network.service

import com.oguzdogdu.network.common.Constants.PAGE_ITEM_LIMIT
import com.oguzdogdu.network.model.collection.CollectionResponse
import com.oguzdogdu.network.model.maindto.Photo
import com.oguzdogdu.network.model.maindto.UnsplashResponseItem
import com.oguzdogdu.network.model.searchdto.SearchResponseItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WallpaperService {

    @GET("photos")
    suspend fun getImagesByOrders(
        @Query("per_page") perPage: Int? = PAGE_ITEM_LIMIT,
        @Query("page") page: Int?,
        @Query("order_by") order: String?,
    ): List<UnsplashResponseItem>

    @GET("photos/{id}")
    suspend fun getPhoto(
        @Path("id") id: String,
    ): UnsplashResponseItem

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("page") page: Int?,
        @Query("query") query: String,
    ): SearchResponseItem

    @GET("collections")
    suspend fun getCollections(
        @Query("page") page: Int?,
    ): List<CollectionResponse>

    @GET("collections/{id}/photos")
    suspend fun getCollectionsListById(
        @Path("id") id: String?,
    ): List<Photo>
}