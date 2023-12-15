package com.oguzdogdu.wallieshd.presentation.topics

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.topics.Topics

sealed class TopicsState {
    data class TopicsListState(
        val topics: PagingData<Topics> = PagingData.empty()
    ) : TopicsState()
}
