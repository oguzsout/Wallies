package com.oguzdogdu.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.oguzdogdu.data.common.Constants.PAGE_ITEM_LIMIT
import com.oguzdogdu.data.common.safeApiCall
import com.oguzdogdu.data.di.Dispatcher
import com.oguzdogdu.data.di.WalliesDispatchers
import com.oguzdogdu.data.source.paging.CollectionByLikesPagingSource
import com.oguzdogdu.data.source.paging.CollectionsByTitlePagingSource
import com.oguzdogdu.data.source.paging.CollectionsPagingSource
import com.oguzdogdu.data.source.paging.LatestPagingSource
import com.oguzdogdu.data.source.paging.PopularPagingSource
import com.oguzdogdu.data.source.paging.SearchPagingSource
import com.oguzdogdu.domain.model.collection.CollectionList
import com.oguzdogdu.domain.model.collection.WallpaperCollections
import com.oguzdogdu.domain.model.favorites.FavoriteImages
import com.oguzdogdu.domain.model.home.HomeListItems
import com.oguzdogdu.domain.model.latest.LatestImage
import com.oguzdogdu.domain.model.popular.PopularImage
import com.oguzdogdu.domain.model.search.SearchPhoto
import com.oguzdogdu.domain.model.singlephoto.Photo
import com.oguzdogdu.domain.model.topics.Topics
import com.oguzdogdu.domain.repository.WallpaperRepository
import com.oguzdogdu.domain.wrapper.Resource
import com.oguzdogdu.network.model.collection.toCollectionDomain
import com.oguzdogdu.network.model.maindto.homieListToDomain
import com.oguzdogdu.network.model.maindto.toDomain
import com.oguzdogdu.network.model.maindto.toDomainModelLatest
import com.oguzdogdu.network.model.maindto.toDomainModelPhoto
import com.oguzdogdu.network.model.maindto.toDomainModelPopular
import com.oguzdogdu.network.model.searchdto.toDomainSearch
import com.oguzdogdu.network.model.topics.toDomainTopics
import com.oguzdogdu.network.service.WallpaperService
import com.oguzdogdu.wallieshd.cache.dao.FavoriteDao
import com.oguzdogdu.wallieshd.cache.entity.FavoriteImage
import com.oguzdogdu.wallieshd.cache.entity.toDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class WallpaperRepositoryImpl @Inject constructor(
    private val service: WallpaperService,
    private val favoriteDao: FavoriteDao,
    @Dispatcher(WalliesDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) :
    WallpaperRepository {
    override suspend fun getImagesByPopulars(): Flow<PagingData<PopularImage>> {
        val pagingConfig = PagingConfig(pageSize = PAGE_ITEM_LIMIT)
        return Pager(
            config = pagingConfig,
            initialKey = 1,
            pagingSourceFactory = { PopularPagingSource(service = service) }
        ).flow.mapNotNull {
            it.map { popular ->
                popular.toDomainModelPopular()
            }
        }
    }

    override suspend fun getImagesByLatest(): Flow<PagingData<LatestImage>> {
        val pagingConfig = PagingConfig(pageSize = PAGE_ITEM_LIMIT)
        return Pager(
            config = pagingConfig,
            initialKey = 1,
            pagingSourceFactory = { LatestPagingSource(service = service) }
        ).flow.mapNotNull {
            it.map { latest ->
                latest.toDomainModelLatest()
            }
        }
    }

    override suspend fun getPopularAndLatestImagesForHomeScreen(type:String?): Flow<Resource<List<HomeListItems>?>> {
        return safeApiCall(ioDispatcher) {
            service.getImagesByOrders(perPage = 10, page = 1, order = type).body()?.map {
                it.homieListToDomain().homeList.second
            }
        }
    }

    override suspend fun getPhoto(id: String): Flow<Resource<Photo?>> {
        return safeApiCall(ioDispatcher) {
            service.getPhoto(id = id).body()?.toDomainModelPhoto()
        }
    }

    override suspend fun searchPhoto(query: String?,language:String?): Flow<PagingData<SearchPhoto>> {
        val pagingConfig = PagingConfig(pageSize = PAGE_ITEM_LIMIT)
        return Pager(
            config = pagingConfig,
            initialKey = 1,
            pagingSourceFactory = { SearchPagingSource(service = service, query = query ?: "",lang = language) }
        ).flow.mapNotNull {
            it.map { search ->
                search.toDomainSearch()
            }
        }
    }

    override suspend fun getCollectionsList(): Flow<PagingData<WallpaperCollections>> {
        val pagingConfig = PagingConfig(pageSize = PAGE_ITEM_LIMIT)
        return Pager(
            config = pagingConfig,
            initialKey = 1,
            pagingSourceFactory = { CollectionsPagingSource(service = service) }
        ).flow.mapNotNull {
            it.map { collection ->
                collection.toCollectionDomain()
            }
        }
    }

    override suspend fun getCollectionsListByTitleSort(): Flow<PagingData<WallpaperCollections>> {
        val pagingConfig = PagingConfig(pageSize = PAGE_ITEM_LIMIT)
        return Pager(
            config = pagingConfig,
            initialKey = 1,
            pagingSourceFactory = { CollectionsByTitlePagingSource(service = service) }
        ).flow.mapNotNull {
            it.map { collection ->
                collection.toCollectionDomain()
            }
        }
    }

    override suspend fun getCollectionsListByLikesSort(): Flow<PagingData<WallpaperCollections>> {
        val pagingConfig = PagingConfig(pageSize = PAGE_ITEM_LIMIT)
        return Pager(
            config = pagingConfig,
            initialKey = 1,
            pagingSourceFactory = { CollectionByLikesPagingSource(service = service) }
        ).flow.mapNotNull {
            it.map { collection ->
                collection.toCollectionDomain()
            }
        }
    }

    override suspend fun getCollectionsListById(id: String?): Flow<Resource<List<CollectionList>?>> {
        return safeApiCall(ioDispatcher){
            service.getCollectionsListById(id).body()?.map {
                it.toDomain() }
        }
    }

    override suspend fun insertImageToFavorites(favorite: FavoriteImages) {
        return favoriteDao.addFavorites(
            FavoriteImage(
                id = favorite.id.orEmpty(),
                url = favorite.url,
                profileImage = favorite.profileImage,
                name = favorite.name,
                portfolioUrl = favorite.portfolioUrl,
                isChecked = favorite.isChecked
            )
        )
    }

    override suspend fun getFavorites(): Flow<List<FavoriteImages>> {
        return favoriteDao.getFavorites().map { result ->
            result.map {
                it.toDomain()
            }
        }
    }

    override suspend fun deleteFavorites(favorite: FavoriteImages) {
        return favoriteDao.deleteFavorite(
            FavoriteImage(
                id = favorite.id.orEmpty(),
                url = favorite.url,
                profileImage = favorite.profileImage,
                name = favorite.name,
                portfolioUrl = favorite.portfolioUrl,
                isChecked = favorite.isChecked
            )
        )
    }

    override suspend fun getTopicsTitle(): Flow<Resource<List<Topics>?>> {
        return safeApiCall(ioDispatcher) {
            service.getTopics().body()?.map {
                it.toDomainTopics()
            }
        }
    }
}