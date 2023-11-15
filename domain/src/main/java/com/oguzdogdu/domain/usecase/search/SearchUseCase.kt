package com.oguzdogdu.domain.usecase.search

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.search.SearchPhoto
import com.oguzdogdu.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val repository: WallpaperRepository) {
    suspend operator fun invoke(query: String?,language:String?): Flow<PagingData<SearchPhoto>> =
        repository.searchPhoto(query,language)
}