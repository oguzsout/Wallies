package com.oguzdogdu.wallies.presentation.popular

sealed class PopularScreenEvent {
    object FetchPopularData : PopularScreenEvent()
}
