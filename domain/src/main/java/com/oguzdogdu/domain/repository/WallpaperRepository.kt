package com.oguzdogdu.domain.repository

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.collection.CollectionList
import com.oguzdogdu.domain.model.favorites.FavoriteImages
import com.oguzdogdu.domain.model.latest.LatestImage
import com.oguzdogdu.domain.model.popular.PopularImage
import com.oguzdogdu.domain.model.search.SearchPhoto
import com.oguzdogdu.domain.model.singlephoto.Photo
import kotlinx.coroutines.flow.Flow

interface WallpaperRepository {
    suspend fun getImagesByPopulars(): Flow<PagingData<PopularImage>>
    suspend fun getImagesByLatest(): Flow<PagingData<LatestImage>>
    suspend fun getPhoto(id: String): Photo
    suspend fun searchPhoto(query: String?): Flow<PagingData<SearchPhoto>>
    suspend fun getCollectionsList(): Flow<PagingData<com.oguzdogdu.domain.model.collection.WallpaperCollections>>
    suspend fun getCollectionsListById(id: String?): List<CollectionList>
    suspend fun insertImageToFavorites(favorite: FavoriteImages)
    suspend fun getFavorites(): Flow<List<FavoriteImages>>
    suspend fun deleteFavorites(favorite: FavoriteImages)
}