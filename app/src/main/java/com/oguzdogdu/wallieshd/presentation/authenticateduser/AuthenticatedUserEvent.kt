package com.oguzdogdu.wallieshd.presentation.authenticateduser

sealed class AuthenticatedUserEvent {
    object FetchUserInfos : AuthenticatedUserEvent()
    object SignOut : AuthenticatedUserEvent()
}
