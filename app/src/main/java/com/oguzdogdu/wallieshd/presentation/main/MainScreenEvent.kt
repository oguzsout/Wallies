package com.oguzdogdu.wallieshd.presentation.main

sealed interface MainScreenEvent {
    object FetchMainScreenUserData : MainScreenEvent
    data class FetchMainScreenList(val type: String?) : MainScreenEvent
}
