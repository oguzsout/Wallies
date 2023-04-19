package com.oguzdogdu.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.oguzdogdu.data.common.Constants
import com.oguzdogdu.data.model.collection.toCollectionDomain
import com.oguzdogdu.data.model.maindto.toDomainModelLatest
import com.oguzdogdu.data.model.maindto.toDomainModelPhoto
import com.oguzdogdu.data.model.maindto.toDomainModelPopular
import com.oguzdogdu.data.model.searchdto.toDomainSearch
import com.oguzdogdu.data.source.paging.CollectionsPagingSource
import com.oguzdogdu.data.source.paging.LatestPagingSource
import com.oguzdogdu.data.source.paging.PopularPagingSource
import com.oguzdogdu.data.source.paging.SearchPagingSource
import com.oguzdogdu.data.source.remote.WallpaperService
import com.oguzdogdu.domain.model.collection.WallpaperCollections
import com.oguzdogdu.domain.model.latest.LatestImage
import com.oguzdogdu.domain.model.popular.PopularImage
import com.oguzdogdu.domain.model.search.SearchPhoto
import com.oguzdogdu.domain.model.singlephoto.Photo
import com.oguzdogdu.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class WallpaperRepositoryImpl @Inject constructor(private val service: WallpaperService) :
    WallpaperRepository {
    override suspend fun getImagesByPopulars(): Flow<PagingData<PopularImage>> {
        val pagingConfig = PagingConfig(pageSize = Constants.PAGE_ITEM_LIMIT)
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { PopularPagingSource(service = service) }
        ).flow.mapNotNull {
            it.map { popular ->
                popular.toDomainModelPopular()
            }
        }
    }

    override suspend fun getImagesByLatest(): Flow<PagingData<LatestImage>> {
        val pagingConfig = PagingConfig(pageSize = Constants.PAGE_ITEM_LIMIT)
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { LatestPagingSource(service = service) }
        ).flow.mapNotNull {
            it.map { latest ->
                latest.toDomainModelLatest()
            }
        }
    }

    override suspend fun getPhoto(id: String): Photo {
        return service.getPhoto(id).toDomainModelPhoto()
    }

    override suspend fun searchPhoto(query: String?): Flow<PagingData<SearchPhoto>> {
        val pagingConfig = PagingConfig(pageSize = Constants.PAGE_ITEM_LIMIT)
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { SearchPagingSource(service = service, query = query ?: "") }
        ).flow.mapNotNull {
            it.map { search ->
                search.toDomainSearch()
            }
        }
    }

    override suspend fun getCollectionsList(): Flow<PagingData<WallpaperCollections>> {
        val pagingConfig = PagingConfig(pageSize = Constants.PAGE_ITEM_LIMIT)
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { CollectionsPagingSource(service = service) }
        ).flow.mapNotNull {
            it.map { collection ->
                collection.toCollectionDomain()
            }
        }
    }
}