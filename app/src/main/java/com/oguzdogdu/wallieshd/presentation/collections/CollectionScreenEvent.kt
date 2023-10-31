package com.oguzdogdu.wallieshd.presentation.collections

sealed class CollectionScreenEvent {
    object FetchLatestData : CollectionScreenEvent()
    object SortByTitles : CollectionScreenEvent()
    object SortByLikes : CollectionScreenEvent()
}
