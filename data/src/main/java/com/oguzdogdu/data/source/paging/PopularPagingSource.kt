package com.oguzdogdu.data.source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.oguzdogdu.data.common.Constants
import com.oguzdogdu.network.model.maindto.UnsplashResponseItem
import com.oguzdogdu.network.service.WallpaperService

class PopularPagingSource(private val service: WallpaperService) :
    PagingSource<Int, UnsplashResponseItem>() {
    override fun getRefreshKey(state: PagingState<Int, UnsplashResponseItem>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashResponseItem> {
        return try {
            val page = params.key ?: 1
            val response = service.getImagesByOrders(page = page, order = Constants.POPULAR).body().orEmpty()
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