package com.oguzdogdu.wallieshd.presentation.latest

sealed class LatestScreenEvent {
    object FetchLatestData : LatestScreenEvent()
}
