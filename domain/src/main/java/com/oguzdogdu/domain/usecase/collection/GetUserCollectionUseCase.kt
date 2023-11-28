package com.oguzdogdu.domain.usecase.collection

import com.oguzdogdu.domain.model.userdetail.UserCollections
import com.oguzdogdu.domain.repository.UnsplashUserRepository
import com.oguzdogdu.domain.wrapper.Resource
import com.oguzdogdu.domain.wrapper.toResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserCollectionUseCase @Inject constructor(private val repository: UnsplashUserRepository) {
    suspend operator fun invoke(username:String?) : Flow<Resource<List<UserCollections>>> {
        return flow {
            emit(repository.getUsersCollections(username = username))
        }.toResource()
    }
}