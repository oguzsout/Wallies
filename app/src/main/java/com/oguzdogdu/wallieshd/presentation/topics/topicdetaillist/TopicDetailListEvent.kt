package com.oguzdogdu.wallieshd.presentation.topics.topicdetaillist

sealed class TopicDetailListEvent {
    data class FetchTopicListData(val idOrSlug: String?) : TopicDetailListEvent()
}
