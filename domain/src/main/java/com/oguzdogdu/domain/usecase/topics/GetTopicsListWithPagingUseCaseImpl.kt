package com.oguzdogdu.domain.usecase.topics

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.topics.Topics
import com.oguzdogdu.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTopicsListWithPagingUseCaseImpl @Inject constructor(private val repository: WallpaperRepository) :
    GetTopicsListWithPagingUseCase {
    override suspend fun invoke(): Flow<PagingData<Topics>> = repository.getTopicsTitleWithPaging()
}