package com.oguzdogdu.wallies.presentation.collectionslistsbyid

import com.oguzdogdu.domain.model.collection.CollectionList

data class CollectionsListsState(
    val isLoading: Boolean = false,
    val collectionsLists: List<CollectionList> = emptyList(),
    val error: String = ""
)
