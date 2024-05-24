package com.oguzdogdu.network.service

import com.oguzdogdu.network.model.collection.CollectionResponse
import com.oguzdogdu.network.model.maindto.Photo
import com.oguzdogdu.network.model.maindto.UnsplashResponseItem
import com.oguzdogdu.network.model.searchdto.SearchResponseItem
import com.oguzdogdu.network.model.topics.CoverPhoto
import com.oguzdogdu.network.model.topics.TopicsResponseItem

interface WallpaperService {

    suspend fun getImagesByOrders(
        perPage: Int?,
        page: Int?,
        order: String?,
    ): List<UnsplashResponseItem>

    suspend fun getPhoto(
        id: String?,
    ): UnsplashResponseItem

    suspend fun searchPhotos(
         page: Int?,
         perPage: Int?,
         query: String,
         language: String?,
    ): SearchResponseItem

    suspend fun getCollections(
         page: Int?,
         perPage: Int?
    ): List<CollectionResponse>

    suspend fun getCollectionsListById(
        id: String?,
    ): List<Photo>

    suspend fun getTopics(
         page: Int?,
         perPage: Int?,
    ): List<TopicsResponseItem>

    suspend fun getTopicList(
         id: String?,
         page: Int?,
         perPage: Int?,
    ): List<CoverPhoto>
}