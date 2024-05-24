package com.oguzdogdu.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.oguzdogdu.data.common.Constants
import com.oguzdogdu.data.common.safeApiCall
import com.oguzdogdu.data.di.Dispatcher
import com.oguzdogdu.data.di.WalliesDispatchers
import com.oguzdogdu.data.source.paging.SearchPagingSource
import com.oguzdogdu.data.source.paging.SearchUsersPagingSource
import com.oguzdogdu.domain.model.search.searchuser.SearchUser
import com.oguzdogdu.domain.model.userdetail.UserCollections
import com.oguzdogdu.domain.model.userdetail.UserDetails
import com.oguzdogdu.domain.model.userdetail.UsersPhotos
import com.oguzdogdu.domain.repository.UnsplashUserRepository
import com.oguzdogdu.domain.wrapper.Resource
import com.oguzdogdu.network.model.collection.toUserCollection
import com.oguzdogdu.network.model.maindto.toDomainUsersPhotos
import com.oguzdogdu.network.model.searchdto.searchuser.SearchUsersResponse
import com.oguzdogdu.network.model.searchdto.searchuser.toSearchUser
import com.oguzdogdu.network.model.searchdto.toDomainSearch
import com.oguzdogdu.network.model.userdetail.toDomain
import com.oguzdogdu.network.service.UnsplashBaseApiService
import com.oguzdogdu.network.service.UnsplashUserService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class UnsplashUserRepositoryImpl @Inject constructor(private val service: UnsplashUserService,@Dispatcher(
    WalliesDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) :
    UnsplashUserRepository {
    override suspend fun getUserDetails(username: String?): Flow<Resource<UserDetails?>> {
        return safeApiCall(ioDispatcher) {
            service.getUserDetailInfos(username = username).toDomain()
        }
    }

    override suspend fun getUsersPhotos(username: String?): Flow<Resource<List<UsersPhotos>?>>{
        return safeApiCall(ioDispatcher) {
            service.getUserPhotos(username = username).map {
                it.toDomainUsersPhotos()
            }
        }
    }

    override suspend fun getUsersCollections(username: String?): Flow<Resource<List<UserCollections>?>> {
        return safeApiCall(dispatcher = ioDispatcher) {
            service.getUserCollections(username = username).map {
                it.toUserCollection()
            }
        }
    }

    override suspend fun getSearchFromUsers(query: String?): Flow<PagingData<SearchUser>> {
        val pagingConfig = PagingConfig(pageSize = Constants.PAGE_ITEM_LIMIT)
        return Pager(
            config = pagingConfig,
            initialKey = 1,
            pagingSourceFactory = { SearchUsersPagingSource(service = service, query = query ?: "") }
        ).flow.mapNotNull {
            it.map { search ->
                search.toSearchUser()
            }
        }
    }
}