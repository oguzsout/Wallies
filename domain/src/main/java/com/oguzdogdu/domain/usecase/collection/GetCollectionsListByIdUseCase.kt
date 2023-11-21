package com.oguzdogdu.domain.usecase.collection

import com.oguzdogdu.domain.model.collection.CollectionList
import com.oguzdogdu.domain.repository.WallpaperRepository
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCollectionsListByIdUseCase @Inject constructor(private val repository: WallpaperRepository) {

   suspend operator fun invoke(id: String?): Flow<Resource<List<CollectionList>?>> {
        return repository.getCollectionsListById(id)
    }
}