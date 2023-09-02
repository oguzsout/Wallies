package com.oguzdogdu.data.source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.oguzdogdu.network.model.searchdto.Result
import com.oguzdogdu.network.service.WallpaperService

class SearchPagingSource(
    private val service: WallpaperService,
    private val query: String,
) : PagingSource<Int, Result>() {

    override fun getRefreshKey(
        state: PagingState<Int, Result>,
    ): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>)
            : LoadResult<Int, Result> {
        return try {
            val page = params.key ?: 1
            val response = service.searchPhotos(page = page, query = query).body()?.results.orEmpty()
            LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}