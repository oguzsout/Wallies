package com.oguzdogdu.domain.usecase.collection

import com.oguzdogdu.domain.model.userdetail.UserCollections
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface GetUserCollectionUseCase {
    suspend operator fun invoke(username:String?) : Flow<Resource<List<UserCollections>?>>
}