package com.oguzdogdu.data.repository

import com.oguzdogdu.data.common.Constants.API_KEY
import com.oguzdogdu.data.model.UnsplashResponseItem
import com.oguzdogdu.data.model.toDomainModel
import com.oguzdogdu.data.source.WallpaperService
import com.oguzdogdu.domain.PopularImage
import com.oguzdogdu.domain.WallpaperRepository
import javax.inject.Inject

class WallpaperRepositoryImpl @Inject constructor(private val service: WallpaperService) : WallpaperRepository {
    override suspend fun getPopularImages(order: String?,page: Int?): List<PopularImage> {
        return service.getPopularImages(order = order, apiKey = API_KEY, page = page).map {
            it.toDomainModel()
        }
    }
}