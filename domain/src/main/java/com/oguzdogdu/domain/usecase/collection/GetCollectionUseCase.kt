package com.oguzdogdu.domain.usecase.collection

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.collection.WallpaperCollections
import kotlinx.coroutines.flow.Flow

interface GetCollectionUseCase {
    suspend operator fun invoke(): Flow<PagingData<WallpaperCollections>>
}