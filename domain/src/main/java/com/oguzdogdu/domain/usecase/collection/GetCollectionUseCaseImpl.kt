package com.oguzdogdu.domain.usecase.collection

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.collection.WallpaperCollections
import com.oguzdogdu.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class GetCollectionUseCaseImpl @Inject constructor(private val repository: WallpaperRepository): GetCollectionUseCase {
    override suspend fun invoke(): Flow<PagingData<WallpaperCollections>> = repository.getCollectionsList().distinctUntilChanged()
}