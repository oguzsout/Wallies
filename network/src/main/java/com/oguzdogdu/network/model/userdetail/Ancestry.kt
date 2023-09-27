package com.oguzdogdu.network.model.userdetail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ancestry(
    val category: Category,
    val subcategory: Subcategory,
):Parcelable