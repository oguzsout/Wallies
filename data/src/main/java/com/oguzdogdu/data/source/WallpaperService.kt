package com.oguzdogdu.data.source

import com.oguzdogdu.data.model.UnsplashResponseItem
import retrofit2.http.GET
import retrofit2.http.Query

interface WallpaperService {

    @GET("photos")
    suspend fun getPopularImages(
        @Query("page") page : Int?,
        @Query("client_id") apiKey : String?,
        @Query("order_by") order: String?
    ): List<UnsplashResponseItem>
}