package com.oguzdogdu.domain.usecase.collection

import com.oguzdogdu.domain.model.collection.CollectionList
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface GetCollectionListByIdUseCase {
    suspend operator fun invoke(id: String?): Flow<Resource<List<CollectionList>?>>
}