package com.oguzdogdu.wallies.presentation.authenticateduser

sealed class AuthenticatedUserEvent {
    object FetchUserInfos : AuthenticatedUserEvent()
    object SignOut : AuthenticatedUserEvent()
}
