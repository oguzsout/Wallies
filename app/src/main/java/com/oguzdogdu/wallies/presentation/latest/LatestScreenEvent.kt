package com.oguzdogdu.wallies.presentation.latest

sealed class LatestScreenEvent {
    object FetchLatestData : LatestScreenEvent()
}
