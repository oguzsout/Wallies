package com.oguzdogdu.wallieshd.presentation.main

sealed interface MainScreenEvent {
    data object FetchMainScreenUserData : MainScreenEvent
    data class FetchMainScreenList(val type: String?) : MainScreenEvent
}
