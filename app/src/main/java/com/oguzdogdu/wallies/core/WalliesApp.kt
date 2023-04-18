package com.oguzdogdu.wallies.core

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.oguzdogdu.wallies.util.LocaleHelper
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class WalliesApp : Application() {

    override fun onCreate() {
        super.onCreate()
        setTheme()
    }
    override fun attachBaseContext(base: Context?) {
        val prefs = base?.let { PreferenceManager.getDefaultSharedPreferences(it) }
        val language = prefs?.getString("language_preference", "en")
        super.attachBaseContext(base?.let { LocaleHelper.setLocale(it, language!!) })
    }

    private fun setTheme() {
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        when (sp.getString("app_theme", "")) {
            "1" -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                )
            }
            "2" -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
                )
            }
        }
    }

}