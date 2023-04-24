package com.oguzdogdu.domain.usecase.collection

import androidx.paging.PagingData
import com.oguzdogdu.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class GetCollectionsUseCase @Inject constructor(private val repository: WallpaperRepository) {
         suspend operator fun invoke(): Flow<PagingData<com.oguzdogdu.domain.model.collection.WallpaperCollections>> {
             return repository.getCollectionsList().distinctUntilChanged()
         }
}

