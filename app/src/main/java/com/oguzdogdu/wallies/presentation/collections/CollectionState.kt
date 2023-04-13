package com.oguzdogdu.wallies.presentation.collections

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.collection.WallpaperCollections

data class CollectionState(
    val isLoading: Boolean = false,
    val collections: PagingData<WallpaperCollections> = PagingData.empty(),
    val error: String = ""
)
