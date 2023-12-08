package com.oguzdogdu.wallieshd.presentation.main

import com.oguzdogdu.domain.model.home.HomeListItems
import com.oguzdogdu.domain.model.topics.Topics

sealed class HomeRecyclerViewItems {
    data class TopicsTitleList(
        val loading: Boolean? = false,
        val error: String? = null,
        val topics: List<Topics>? = emptyList()
    ) : HomeRecyclerViewItems()
    data class PopularAndLatestImageList(
        val loading: Boolean? = false,
        val error: String? = null,
        val list: Pair<String?, List<HomeListItems>?>? = null
    ) : HomeRecyclerViewItems()
}
