package com.oguzdogdu.wallies.presentation.authenticateduser

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ProfileMenu constructor(
    @DrawableRes val iconRes: Int? = null,
    @StringRes val titleRes: Int? = null,
    var subTitle: String? = null
)
