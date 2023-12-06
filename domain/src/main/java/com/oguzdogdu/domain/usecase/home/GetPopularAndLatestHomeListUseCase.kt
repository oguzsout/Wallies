package com.oguzdogdu.domain.usecase.home

import com.oguzdogdu.domain.model.home.HomeListItems
import com.oguzdogdu.domain.repository.WallpaperRepository
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPopularAndLatestHomeListUseCase @Inject constructor(
    private val repository: WallpaperRepository,
) {
    suspend operator fun invoke(type:String?): Flow<Resource<List<HomeListItems>?>> {
        return repository.getPopularAndLatestImagesForHomeScreen(type)
    }
}