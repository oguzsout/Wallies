package com.oguzdogdu.wallieshd.presentation.main

sealed class MainScreenState {
    data class UserProfile(val profileImage: String?) : MainScreenState()
    data class UserAuthenticated(val isAuthenticated: Boolean?) : MainScreenState()
}
