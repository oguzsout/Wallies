package com.oguzdogdu.domain.usecase.popular

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.popular.PopularImage
import com.oguzdogdu.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPopularUseCase @Inject constructor(
    private val repository: WallpaperRepository,
) {
    suspend operator fun invoke(): Flow<PagingData<PopularImage>> = repository.getImagesByPopulars()
}