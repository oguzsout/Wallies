package com.oguzdogdu.wallies.presentation.main

sealed class MainScreenState {
    data class UserProfile(val profileImage: String?) : MainScreenState()
}
