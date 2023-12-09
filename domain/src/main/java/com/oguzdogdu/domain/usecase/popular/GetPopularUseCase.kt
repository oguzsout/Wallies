package com.oguzdogdu.domain.usecase.popular

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.popular.PopularImage
import kotlinx.coroutines.flow.Flow

interface GetPopularUseCase {
    suspend operator fun invoke(): Flow<PagingData<PopularImage>>
}