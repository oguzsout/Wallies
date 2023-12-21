package com.oguzdogdu.wallieshd.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation

fun ImageView.loadImage(
    url: String?,
    placeholder: Drawable? = null,
    error: Drawable? = null,
    radius: Boolean? = false,
    cacheEnable: Boolean? = true

) {
    this.load(url) {
        when (cacheEnable) {
            false -> {
                diskCachePolicy(CachePolicy.DISABLED)
                memoryCachePolicy(CachePolicy.DISABLED)
            }
            true -> {
                diskCachePolicy(CachePolicy.ENABLED)
                memoryCachePolicy(CachePolicy.ENABLED)
            }

            null -> return
        }
        if (radius == true) {
            this.transformations(CircleCropTransformation())
        }
        placeholder(placeholder)
        error(error)
    }
}
