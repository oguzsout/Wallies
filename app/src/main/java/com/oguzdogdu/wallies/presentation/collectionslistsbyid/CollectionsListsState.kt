package com.oguzdogdu.wallies.presentation.collectionslistsbyid

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.collection.CollectionList
import com.oguzdogdu.domain.model.collection.WallpaperCollections

data class CollectionsListsState(
    val isLoading: Boolean = false,
    val collectionsLists: List<CollectionList> = emptyList(),
    val error: String = ""
)
