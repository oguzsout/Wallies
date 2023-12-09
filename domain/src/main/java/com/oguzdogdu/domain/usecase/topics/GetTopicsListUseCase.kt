package com.oguzdogdu.domain.usecase.topics

import com.oguzdogdu.domain.model.topics.Topics
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface GetTopicsListUseCase {
    suspend operator fun invoke(): Flow<Resource<List<Topics>?>>
}