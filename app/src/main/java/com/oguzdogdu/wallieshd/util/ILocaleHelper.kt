package com.oguzdogdu.wallieshd.util

import android.annotation.TargetApi
import android.content.Context
import android.os.Build

interface ILocaleHelper {
    fun onAttach(defaultLanguage: String): Context?

    fun setLocale(language: String): Context?

    @TargetApi(Build.VERSION_CODES.N)
    fun updateResources(language: String): Context

    fun updateResourcesLegacy(language: String): Context
}
