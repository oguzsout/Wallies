package com.oguzdogdu.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState

class SearchPagingSource(
    private val service: WallpaperService,
    private val query: String
) : PagingSource<Int, com.oguzdogdu.data.model.searchdto.Result>() {

    override fun getRefreshKey(
        state: PagingState<Int, com.oguzdogdu.data.model.searchdto.Result>
    ): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>)
            : LoadResult<Int, com.oguzdogdu.data.model.searchdto.Result> {
        return try {
            val page = params.key ?: 1
            val response = service.searchPhotos(page = page, query = query)
            LoadResult.Page(
                data = response.results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}