package com.oguzdogdu.wallies.presentation.authenticateduser

sealed class AuthenticatedUserScreenState {
    object Loading : AuthenticatedUserScreenState()
    data class UserInfoError(val errorMessage: String?) : AuthenticatedUserScreenState()
    data class CheckUserAuthStatus(val isAuthenticated: Boolean) : AuthenticatedUserScreenState()
    data class CheckUserGoogleSignIn(val isAuthenticated: Boolean) : AuthenticatedUserScreenState()
    data class UserInfos(
        val name: String? = null,
        val surname: String? = null,
        val email: String? = null,
        val profileImage: String? = null
    ) : AuthenticatedUserScreenState()
}
