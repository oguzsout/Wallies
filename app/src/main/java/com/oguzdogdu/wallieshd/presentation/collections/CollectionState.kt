package com.oguzdogdu.wallieshd.presentation.collections

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.collection.WallpaperCollections

sealed class CollectionState {
    data class ItemState(
        val collections: PagingData<WallpaperCollections> = PagingData.empty()
    ) : CollectionState()
    data class SortedByTitle(
        val collections: PagingData<WallpaperCollections> = PagingData.empty()
    ) : CollectionState()
}
