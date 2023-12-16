package com.oguzdogdu.wallieshd.presentation.topics.topicdetaillist

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.topics.TopicDetail

sealed class TopicDetailListState {
    data class TopicListState(val topicList: PagingData<TopicDetail> = PagingData.empty()) :
        TopicDetailListState()
}
