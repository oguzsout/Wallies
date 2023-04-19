package com.oguzdogdu.domain.usecase.popular

import androidx.paging.PagingData
import com.oguzdogdu.domain.Resource
import com.oguzdogdu.domain.repository.WallpaperRepository
import com.oguzdogdu.domain.model.popular.PopularImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetPopularUseCase @Inject constructor(
    private val repository: WallpaperRepository
) {
    suspend operator fun invoke(): Flow<PagingData<PopularImage>> {
        return repository.getImagesByPopulars().distinctUntilChanged()
    }
}