package com.oguzdogdu.domain.usecase.topics.topicdetail

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.topics.TopicDetail
import kotlinx.coroutines.flow.Flow

interface GetTopicDetailListUseCase {
    suspend operator fun invoke(idOrSlug:String?): Flow<PagingData<TopicDetail>>
}