package com.oguzdogdu.wallieshd.presentation.profiledetail

import android.graphics.drawable.Drawable

data class UserSocialAccountsMenu(
    val title: String,
    val titleIcon: Drawable,
    val menuItemType: MenuItemType
) {
    enum class MenuItemType {
        INSTAGRAM,
        TWITTER,
        PORTFOLIO
    }
}
