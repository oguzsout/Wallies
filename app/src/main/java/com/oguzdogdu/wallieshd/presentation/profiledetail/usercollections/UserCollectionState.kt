package com.oguzdogdu.wallieshd.presentation.profiledetail.usercollections

sealed interface UserCollectionState {
    object Loading : UserCollectionState
    data class UserCollectionError(val errorMessage: String?) : UserCollectionState
    data class UserCollections(
        val usersCollection: List<com.oguzdogdu.domain.model.userdetail.UserCollections>?
    ) : UserCollectionState
}
