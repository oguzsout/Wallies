package com.oguzdogdu.wallieshd.presentation.main

sealed interface MainScreenEvent {
    object FetchMainScreenUserData : MainScreenEvent
}
