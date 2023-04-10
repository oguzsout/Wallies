package com.oguzdogdu.data.source

import com.oguzdogdu.data.BuildConfig
import com.oguzdogdu.data.common.Constants
import com.oguzdogdu.data.model.maindto.UnsplashResponseItem
import com.oguzdogdu.data.model.searchdto.SearchResponseItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WallpaperService {

    @GET("photos")
    suspend fun getImagesByOrders(
        @Query("per_page") perPage: Int? = 30,
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
}