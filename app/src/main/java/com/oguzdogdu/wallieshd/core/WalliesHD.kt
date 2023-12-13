package com.oguzdogdu.wallieshd.core

import android.app.Application
import coil.Coil
import coil.ImageLoader
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WalliesHD : Application() {

    override fun onCreate() {
        super.onCreate()
        setCoilImageLoader()
    }

    private fun setCoilImageLoader() {
        val imageLoader = ImageLoader.Builder(applicationContext)
            .respectCacheHeaders(false)
            .build()
        Coil.setImageLoader(imageLoader)
    }
}
