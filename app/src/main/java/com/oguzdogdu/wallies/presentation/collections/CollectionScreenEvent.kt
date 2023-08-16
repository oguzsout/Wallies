package com.oguzdogdu.wallies.presentation.collections

sealed class CollectionScreenEvent {
    object FetchLatestData : CollectionScreenEvent()
}
