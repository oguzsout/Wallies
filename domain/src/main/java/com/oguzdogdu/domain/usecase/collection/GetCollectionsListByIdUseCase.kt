package com.oguzdogdu.domain.usecase.collection

import com.oguzdogdu.domain.wrapper.Resource
import com.oguzdogdu.domain.model.collection.CollectionList
import com.oguzdogdu.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetCollectionsListByIdUseCase @Inject constructor(private val repository: WallpaperRepository) {

    operator fun invoke(id: String?): Flow<Resource<List<CollectionList>>> = flow {

        try {
            emit(Resource.Loading)
            emit(Resource.Success(repository.getCollectionsListById(id)))
        } catch (e: IOException) {
            emit(Resource.Error(e.localizedMessage.orEmpty()))
        }
    }
}