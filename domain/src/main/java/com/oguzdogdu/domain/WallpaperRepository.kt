package com.oguzdogdu.domain

interface WallpaperRepository {
    suspend fun getImagesByPopulars(page: Int?): List<PopularImage>
    suspend fun getImagesByLatest(page: Int?): List<LatestImage>
}