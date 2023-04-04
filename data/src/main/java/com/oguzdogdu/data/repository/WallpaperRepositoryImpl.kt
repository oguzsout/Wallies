package com.oguzdogdu.data.repository

import com.oguzdogdu.data.common.Constants
import com.oguzdogdu.data.common.Constants.API_KEY
import com.oguzdogdu.data.model.toDomainModelLatest
import com.oguzdogdu.data.model.toDomainModelPopular
import com.oguzdogdu.data.source.WallpaperService
import com.oguzdogdu.domain.LatestImage
import com.oguzdogdu.domain.PopularImage
import com.oguzdogdu.domain.WallpaperRepository
import javax.inject.Inject

class WallpaperRepositoryImpl @Inject constructor(private val service: WallpaperService) : WallpaperRepository {
    override suspend fun getImagesByPopulars(page: Int?): List<PopularImage> {
        return service.getImagesByOrders(order = Constants.POPULAR, apiKey = API_KEY, page = page).map {
            it.toDomainModelPopular()
        }
    }

    override suspend fun getImagesByLatest( page: Int?): List<LatestImage> {
        return service.getImagesByOrders(order = Constants.LATEST, apiKey = API_KEY, page = page).map {
            it.toDomainModelLatest()
        }
    }
}