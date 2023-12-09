package com.oguzdogdu.domain.usecase.popular

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.popular.PopularImage
import com.oguzdogdu.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPopularUseCaseImpl @Inject constructor(
    private val repository: WallpaperRepository,
) : GetPopularUseCase {
    override suspend fun invoke(): Flow<PagingData<PopularImage>> = repository.getImagesByPopulars()
}