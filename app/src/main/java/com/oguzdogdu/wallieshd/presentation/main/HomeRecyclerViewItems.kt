package com.oguzdogdu.wallieshd.presentation.main

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.latest.LatestImage
import com.oguzdogdu.domain.model.popular.PopularImage
import com.oguzdogdu.domain.model.topics.Topics

sealed class HomeRecyclerViewItems {
    data class TopicsTitleList(val topics: List<Topics>?) : HomeRecyclerViewItems()
    data class PopularImageList(val popular: PagingData<PopularImage> = PagingData.empty()) : HomeRecyclerViewItems()
    data class LatestImageList(val latest: List<LatestImage>?) : HomeRecyclerViewItems()
}
