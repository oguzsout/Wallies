package com.oguzdogdu.wallieshd.util

import android.content.Context
import android.os.Build
import java.util.*

class LocaleHelper(private val context: Context) : ILocaleHelper {

    override fun onAttach(defaultLanguage: String): Context {
        return setLocale(defaultLanguage)
    }

    override fun setLocale(language: String): Context {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(language)
        } else {
            updateResourcesLegacy(language)
        }
    }

    override fun updateResources(language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }

    override fun updateResourcesLegacy(language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val resources = context.resources

        val configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
}
