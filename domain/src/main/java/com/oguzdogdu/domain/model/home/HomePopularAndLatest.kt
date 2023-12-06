package com.oguzdogdu.domain.model.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomePopularAndLatest(
    val homeList: Pair<String?,HomeListItems>
) : Parcelable {
    enum class ListType(val type: String) {
        POPULAR("popular"),
        LATEST("latest");

        companion object {
            fun fromType(type: String): ListType? {
                return values().find { it.type == type }
            }
        }
    }
}
