package com.oguzdogdu.domain.repository

import com.oguzdogdu.domain.model.latest.LatestImage
import com.oguzdogdu.domain.model.popular.PopularImage
import com.oguzdogdu.domain.model.singlephoto.Photo

interface WallpaperRepository {
    suspend fun getImagesByPopulars(page: Int?): List<PopularImage>
    suspend fun getImagesByLatest(page: Int?): List<LatestImage>

    suspend fun getPhoto(id:String) : Photo
}