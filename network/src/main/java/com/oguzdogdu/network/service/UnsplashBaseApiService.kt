package com.oguzdogdu.network.service

import com.oguzdogdu.network.model.collection.CollectionResponse
import com.oguzdogdu.network.model.maindto.Photo
import com.oguzdogdu.network.model.maindto.UnsplashResponseItem
import com.oguzdogdu.network.model.searchdto.SearchResponseItem
import com.oguzdogdu.network.model.searchdto.searchuser.SearchUsersResponse
import com.oguzdogdu.network.model.topics.CoverPhoto
import com.oguzdogdu.network.model.topics.TopicsResponseItem
import com.oguzdogdu.network.model.userdetail.UserDetailResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import javax.inject.Inject

class UnsplashBaseApiService @Inject constructor(private val client: HttpClient) : WallpaperService,
    UnsplashUserService {
    override suspend fun getUserDetailInfos(username: String?): UserDetailResponse {
        return client.get("users/${username}").body<UserDetailResponse>()
    }

    override suspend fun getUserPhotos(username: String?): List<Photo> {
        return client.get("users/${username}/photos").body<List<Photo>>()
    }

    override suspend fun getUserCollections(username: String?): List<CollectionResponse> {
        return client.get("users/${username}/collections").body<List<CollectionResponse>>()
    }

    override suspend fun getSearchUserData(page: Int?, query: String?): SearchUsersResponse {
        return client.get {
            url("search/users")
            parameter("query", query)
            parameter("page", page)
        }.body<SearchUsersResponse>()
    }

    override suspend fun getImagesByOrders(
        perPage: Int?, page: Int?, order: String?
    ): List<UnsplashResponseItem> {
        return client.get {
            url("photos")
            parameter("per_page", perPage)
            parameter("page", page)
            parameter("order_by", order)
        }.body<List<UnsplashResponseItem>>()
    }

    override suspend fun getPhoto(id: String?): UnsplashResponseItem {
        return client.get("photos/${id}").body<UnsplashResponseItem>()
    }

    override suspend fun searchPhotos(
        page: Int?, perPage: Int?, query: String, language: String?
    ): SearchResponseItem {
        return client.get {
            url("search/photos")
            parameter("query", query)
            parameter("per_page", perPage)
            parameter("page", page)
            parameter("lang", language)
        }.body<SearchResponseItem>()
    }

    override suspend fun getCollections(page: Int?, perPage: Int?): List<CollectionResponse> {
        return client.get {
            url("collections")
            parameter("per_page", perPage)
            parameter("page", page)
        }.body<List<CollectionResponse>>()
    }

    override suspend fun getCollectionsListById(id: String?): List<Photo> {
        return client.get("collections/${id}/photos").body<List<Photo>>()
    }

    override suspend fun getTopics(page: Int?, perPage: Int?): List<TopicsResponseItem> {
        return client.get {
            url("topics")
            parameter("per_page", perPage)
            parameter("page", page)
        }.body<List<TopicsResponseItem>>()
    }

    override suspend fun getTopicList(id: String?, page: Int?, perPage: Int?): List<CoverPhoto> {
        return client.get {
            url("topics/${id}/photos")
            parameter("per_page", perPage)
            parameter("page", page)
        }.body<List<CoverPhoto>>()
    }
}