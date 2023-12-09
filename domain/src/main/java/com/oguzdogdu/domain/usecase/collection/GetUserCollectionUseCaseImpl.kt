package com.oguzdogdu.domain.usecase.collection

import com.oguzdogdu.domain.model.userdetail.UserCollections
import com.oguzdogdu.domain.repository.UnsplashUserRepository
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserCollectionUseCaseImpl @Inject constructor(private val repository: UnsplashUserRepository) :
    GetUserCollectionUseCase {
    override suspend fun invoke(username: String?): Flow<Resource<List<UserCollections>?>> =
        repository.getUsersCollections(username = username)
}