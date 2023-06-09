package com.oguzdogdu.wallies.presentation.settings

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.util.ThemeKeys
import com.oguzdogdu.wallies.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@Suppress("DEPRECATION")
@AndroidEntryPoint
class SettingsFragment :
    PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private val viewModel: SettingsViewModel by viewModels()

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val buttonPreference = Preference(requireContext()).apply {
            key = "button_preference"
            title = "Çıkış Yap"
            icon = resources.getDrawable(R.drawable.ic_log_out)
        }
        preferenceScreen.addPreference(buttonPreference)
        buttonPreference.setOnPreferenceClickListener {
            when (viewModel.loginState.value) {
                SettingsState.SignOut -> {
                    viewModel.signOut()
                    findNavController().navigate(R.id.toLogin)
                }
            }
            true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "app_theme") {
            when (sharedPreferences?.getString(key, ThemeKeys.SYSTEM_THEME.value)) {
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
        if (key == "language_preference") {
            loadLocale()
        }
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            "clear_cache" -> {
                context?.cacheDir?.deleteRecursively()
                requireView().showToast(requireContext(), R.string.cache_state_string)
            }
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun loadLocale() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val language = prefs.getString("language_preference", "en")
        setLocale(language)
    }

    private fun setLocale(language: String?) {
        val locale = language?.let { Locale(it) }
        if (locale != null) {
            Locale.setDefault(locale)
        }
        val config = Configuration()
        config.setLocale(locale)
        requireContext().resources.updateConfiguration(
            config,
            requireContext().resources.displayMetrics
        )
        activity?.recreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .unregisterOnSharedPreferenceChangeListener(this)
    }
}
