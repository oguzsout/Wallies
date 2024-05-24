package com.oguzdogdu.data.source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.oguzdogdu.network.model.topics.CoverPhoto
import com.oguzdogdu.network.service.WallpaperService

class TopicListSource (private val service: WallpaperService,private val idOrSlug:String?) :
    PagingSource<Int, CoverPhoto>() {
    override fun getRefreshKey(state: PagingState<Int, CoverPhoto>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CoverPhoto> {
        return try {
            val page = params.key ?: 1
            val response = service.getTopicList(
                id = idOrSlug,
                perPage = 10, page = page
            )
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