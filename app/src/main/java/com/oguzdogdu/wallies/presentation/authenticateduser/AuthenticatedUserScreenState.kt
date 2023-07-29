package com.oguzdogdu.wallies.presentation.authenticateduser

sealed class AuthenticatedUserScreenState {
    object Loading : AuthenticatedUserScreenState()
    data class UserInfoError(val errorMessage: String?) : AuthenticatedUserScreenState()
    data class CheckUserAuthStatus(val isAuthenticated: Boolean) : AuthenticatedUserScreenState()
    data class UserInfos(
        val name: String?,
        val surname: String?,
        val email: String?,
        val profileImage: String?
    ) : AuthenticatedUserScreenState()
}
