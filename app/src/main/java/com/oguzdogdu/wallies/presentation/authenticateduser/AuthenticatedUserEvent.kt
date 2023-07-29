package com.oguzdogdu.wallies.presentation.authenticateduser

sealed class AuthenticatedUserEvent {
    object SignOut : AuthenticatedUserEvent()
}
