package com.oguzdogdu.wallies.presentation.collectionslistsbyid

sealed class CollectionListEvent {
    data class FetchCollectionList(val id: String?) : CollectionListEvent()
}
