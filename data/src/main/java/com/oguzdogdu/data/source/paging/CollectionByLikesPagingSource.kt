package com.oguzdogdu.data.source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.oguzdogdu.network.common.Constants
import com.oguzdogdu.network.model.collection.CollectionResponse
import com.oguzdogdu.network.service.WallpaperService

class CollectionByLikesPagingSource(private val service: WallpaperService) :
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
            val response =
                service.getCollections(page = page, perPage = Constants.PAGE_ITEM_LIMIT).body()
                    .orEmpty()
            val filteredAndSortedResponse = response.sortedBy { it.cover_photo?.likes }
            LoadResult.Page(
                data = filteredAndSortedResponse,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}