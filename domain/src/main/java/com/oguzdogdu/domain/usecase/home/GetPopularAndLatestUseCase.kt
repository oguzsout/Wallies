package com.oguzdogdu.domain.usecase.home

import com.oguzdogdu.domain.model.home.HomeListItems
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface GetPopularAndLatestUseCase {
   suspend operator fun invoke(type:String?): Flow<Resource<List<HomeListItems>?>>
}