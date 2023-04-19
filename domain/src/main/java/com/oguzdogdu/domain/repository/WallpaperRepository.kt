package com.oguzdogdu.domain.repository

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.latest.LatestImage
import com.oguzdogdu.domain.model.popular.PopularImage
import com.oguzdogdu.domain.model.search.SearchPhoto
import com.oguzdogdu.domain.model.singlephoto.Photo
import kotlinx.coroutines.flow.Flow

interface WallpaperRepository {
    suspend fun getImagesByPopulars(): Flow<PagingData<PopularImage>>
    suspend fun getImagesByLatest(page: Int?): List<LatestImage>
    suspend fun getPhoto(id:String) : Photo
    suspend fun searchPhoto(query:String?) : Flow<PagingData<SearchPhoto>>
    suspend fun getCollectionsList() : Flow<PagingData<com.oguzdogdu.domain.model.collection.WallpaperCollections>>
}