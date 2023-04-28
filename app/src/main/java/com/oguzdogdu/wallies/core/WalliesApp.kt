package com.oguzdogdu.wallies.core

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.oguzdogdu.wallies.util.LocaleHelper
import com.oguzdogdu.wallies.util.ThemeKeys
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import javax.inject.Inject


@HiltAndroidApp
class WalliesApp : Application() {

    @Inject
    lateinit var okHttpClient: OkHttpClient

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
        when (sp.getString("app_theme", ThemeKeys.SYSTEM_THEME.value)) {
            ThemeKeys.LIGHT_THEME.value -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                )
            }

            ThemeKeys.DARK_THEME.value -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
                )
            }

            ThemeKeys.SYSTEM_THEME.value -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                )
            }
        }
    }
}