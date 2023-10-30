package com.oguzdogdu.wallieshd.presentation.collections

data class MenuAdapterItem(
    val title: String,
    val menuItemType: MenuItemType
) {
    enum class MenuItemType {
        ALPHABETIC,
        LIKE
    }
}
