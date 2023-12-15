package com.oguzdogdu.domain.usecase.topics

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.topics.Topics
import kotlinx.coroutines.flow.Flow

interface GetTopicsListWithPagingUseCase {
    suspend operator fun invoke(): Flow<PagingData<Topics>>
}