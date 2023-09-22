package com.oguzdogdu.wallieshd.presentation.collectionslistsbyid

sealed class CollectionListEvent {
    data class FetchCollectionList(val id: String?) : CollectionListEvent()
}
