package com.oguzdogdu.wallieshd.util

import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.presentation.authenticateduser.ProfileMenu

object OptionLists {
    val profileOptionsList = listOf(
        ProfileMenu(
            iconRes = R.drawable.ic_person,
            titleRes = R.string.edit_user_info_title
        ),
        ProfileMenu(
            iconRes = R.drawable.ic_email,
            titleRes = R.string.edit_email_title
        ),
        ProfileMenu(
            iconRes = R.drawable.password,
            titleRes = R.string.forgot_password_title
        )
    )

    val appOptionsList = listOf(
        ProfileMenu(
            iconRes = R.drawable.dark_mode,
            titleRes = R.string.theme_text
        ),
        ProfileMenu(
            iconRes = R.drawable.language,
            titleRes = R.string.language_title_text
        ),
        ProfileMenu(
            iconRes = R.drawable.cache,
            titleRes = R.string.clear_cache_title
        )
    )
}
