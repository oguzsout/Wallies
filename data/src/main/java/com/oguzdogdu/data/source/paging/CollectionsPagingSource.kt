package com.oguzdogdu.data.source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.oguzdogdu.data.model.collection.CollectionResponse
import com.oguzdogdu.data.source.remote.WallpaperService

class CollectionsPagingSource(private val service: WallpaperService) :
    PagingSource<Int, CollectionResponse>() {
    override fun getRefreshKey(state: PagingState<Int, CollectionResponse>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CollectionResponse> {
        return try {
            val page = params.key ?: 1
            val response = service.getCollections(page = page)
            LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page - 1,
                nextKey = page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}