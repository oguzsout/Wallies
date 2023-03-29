package com.oguzdogdu.domain

interface WallpaperRepository {
    suspend fun getPopularImages(order:String?,page: Int?): List<PopularImage>
}