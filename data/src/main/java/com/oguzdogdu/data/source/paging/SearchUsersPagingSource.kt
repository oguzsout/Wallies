package com.oguzdogdu.data.source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.oguzdogdu.network.service.UnsplashUserService

class SearchUsersPagingSource(
    private val service: UnsplashUserService,
    private val query: String,
) : PagingSource<Int, com.oguzdogdu.network.model.searchdto.searchuser.Result>() {

    override fun getRefreshKey(
        state: PagingState<Int, com.oguzdogdu.network.model.searchdto.searchuser.Result>,
    ): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>)
            : LoadResult<Int, com.oguzdogdu.network.model.searchdto.searchuser.Result> {
        return try {
            val page = params.key ?: 1
            val response = service.getSearchUserData(
                page = page,
                query = query
            ).body()?.results.orEmpty()
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