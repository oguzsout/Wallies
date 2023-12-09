package com.oguzdogdu.domain.usecase.latest

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.latest.LatestImage
import kotlinx.coroutines.flow.Flow

interface GetLatestUseCases {
    suspend operator fun invoke(): Flow<PagingData<LatestImage>>
}