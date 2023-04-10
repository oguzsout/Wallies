package com.oguzdogdu.domain.usecase.search

import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.flatMap
import androidx.paging.map
import com.oguzdogdu.domain.Resource
import com.oguzdogdu.domain.model.search.SearchPhoto
import com.oguzdogdu.domain.repository.WallpaperRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.io.IOException
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val repository: WallpaperRepository) {
    suspend operator fun invoke(query:String) : Flow<PagingData<SearchPhoto>> {
      return repository.searchPhoto(query).distinctUntilChanged()
    }
}