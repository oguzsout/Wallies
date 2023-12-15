package com.oguzdogdu.data.source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.oguzdogdu.network.model.topics.TopicsResponseItem
import com.oguzdogdu.network.service.WallpaperService

class TopicsPagingSource(private val service: WallpaperService) :
    PagingSource<Int, TopicsResponseItem>() {
    override fun getRefreshKey(state: PagingState<Int, TopicsResponseItem>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TopicsResponseItem> {
        return try {
            val page = params.key ?: 1
            val response = service.getTopics(
                perPage = 10, page = page
            ).body()
            LoadResult.Page(
                data = response.orEmpty(),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response?.isEmpty() == true) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}