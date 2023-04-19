package com.oguzdogdu.domain.usecase.latest

import androidx.paging.PagingData
import com.oguzdogdu.domain.repository.WallpaperRepository
import com.oguzdogdu.domain.model.latest.LatestImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class GetLatestUseCase @Inject constructor(
    private val repository: WallpaperRepository
) {
    suspend operator fun invoke(): Flow<PagingData<LatestImage>> {
        return repository.getImagesByLatest().distinctUntilChanged()
    }
}