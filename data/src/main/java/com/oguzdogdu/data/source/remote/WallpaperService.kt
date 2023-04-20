package com.oguzdogdu.data.source.remote

import com.oguzdogdu.data.common.Constants.PAGE_ITEM_LIMIT
import com.oguzdogdu.data.model.collection.CollectionResponse
import com.oguzdogdu.data.model.collection.CollectionResponseItem
import com.oguzdogdu.data.model.maindto.UnsplashResponseItem
import com.oguzdogdu.data.model.searchdto.SearchResponseItem
import com.oguzdogdu.domain.model.singlephoto.Photo
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
        @Path("id") id: String?
    ): List<com.oguzdogdu.data.model.maindto.Photo>
}