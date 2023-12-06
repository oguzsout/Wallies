package com.oguzdogdu.domain.model.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeListItems(
    val id: String?,
    val url: String?,
) : Parcelable
