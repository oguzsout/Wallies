package com.oguzdogdu.domain.usecase.search.searchuser

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.search.searchuser.SearchUser
import com.oguzdogdu.domain.repository.UnsplashUserRepository
import com.oguzdogdu.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchUserUseCaseImpl @Inject constructor(private val repository: UnsplashUserRepository) :
    GetSearchUserUseCase {
    override suspend fun invoke(query: String?): Flow<PagingData<SearchUser>> =
        repository.getSearchFromUsers(query = query)
}