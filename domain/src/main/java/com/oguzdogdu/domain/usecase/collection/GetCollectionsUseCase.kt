package com.oguzdogdu.domain.usecase.collection

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.oguzdogdu.domain.Resource
import com.oguzdogdu.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import java.io.IOException
import javax.inject.Inject

class GetCollectionsUseCase @Inject constructor(private val repository: WallpaperRepository) {
         suspend operator fun invoke(): Flow<PagingData<com.oguzdogdu.domain.model.collection.WallpaperCollections>> {
             return repository.getCollectionsList().distinctUntilChanged()
         }
}

