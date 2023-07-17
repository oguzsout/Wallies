package com.oguzdogdu.wallies.presentation.profiledetail

import com.oguzdogdu.domain.model.userdetail.UserDetails

data class ProfileDetailState(
    val loading: Boolean? = null,
    val userDetails: UserDetails? = null,
    val errorMessage: String? = null
)
