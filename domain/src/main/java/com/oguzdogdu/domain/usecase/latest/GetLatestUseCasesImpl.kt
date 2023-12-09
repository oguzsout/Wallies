package com.oguzdogdu.domain.usecase.latest

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.latest.LatestImage
import com.oguzdogdu.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLatestUseCasesImpl @Inject constructor(private val repository: WallpaperRepository): GetLatestUseCases {
    override suspend fun invoke(): Flow<PagingData<LatestImage>> = repository.getImagesByLatest()
}