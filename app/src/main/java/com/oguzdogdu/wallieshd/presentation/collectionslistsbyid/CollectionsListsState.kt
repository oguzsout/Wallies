package com.oguzdogdu.wallieshd.presentation.collectionslistsbyid

sealed class CollectionsListsState {
    object Loading : CollectionsListsState()
    data class CollectionListError(val errorMessage: String?) : CollectionsListsState()
    data class CollectionList(
        val collectionsLists: List<com.oguzdogdu.domain.model.collection.CollectionList>?
    ) : CollectionsListsState()
}
