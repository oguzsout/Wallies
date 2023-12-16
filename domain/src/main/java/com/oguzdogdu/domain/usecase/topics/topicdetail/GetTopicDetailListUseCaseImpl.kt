package com.oguzdogdu.domain.usecase.topics.topicdetail

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.topics.TopicDetail
import com.oguzdogdu.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTopicDetailListUseCaseImpl @Inject constructor(private val repository: WallpaperRepository) :
    GetTopicDetailListUseCase {
    override suspend fun invoke(idOrSlug: String?): Flow<PagingData<TopicDetail>> =
        repository.getTopicListWithPaging(idOrSlug = idOrSlug)
}