package com.oguzdogdu.domain.repository

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.search.searchuser.SearchUser
import com.oguzdogdu.domain.model.userdetail.UserCollections
import com.oguzdogdu.domain.model.userdetail.UserDetails
import com.oguzdogdu.domain.model.userdetail.UsersPhotos
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface UnsplashUserRepository {
suspend fun getUserDetails(username:String?): Flow<Resource<UserDetails?>>
suspend fun getUsersPhotos(username: String?): Flow<Resource<List<UsersPhotos>?>>
suspend fun getUsersCollections(username: String?): Flow<Resource<List<UserCollections>?>>
suspend fun getSearchFromUsers(query: String?): Flow<PagingData<SearchUser>>
}