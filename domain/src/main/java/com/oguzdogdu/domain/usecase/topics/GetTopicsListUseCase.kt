package com.oguzdogdu.domain.usecase.topics

import com.oguzdogdu.domain.model.topics.Topics
import com.oguzdogdu.domain.repository.WallpaperRepository
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTopicsListUseCase @Inject constructor(
    private val repository: WallpaperRepository,
) {
    suspend operator fun invoke(): Flow<Resource<List<Topics>?>> = repository.getTopicsTitle()
}