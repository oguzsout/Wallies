package com.oguzdogdu.wallieshd.presentation.main

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.databinding.ActivityMainBinding
import com.oguzdogdu.wallieshd.presentation.settings.LanguageValues
import com.oguzdogdu.wallieshd.presentation.settings.SettingsViewModel
import com.oguzdogdu.wallieshd.presentation.settings.ThemeValues
import com.oguzdogdu.wallieshd.util.LocaleHelper
import com.oguzdogdu.wallieshd.util.hide
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.show
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private val appBarConfiguration = AppBarConfiguration(
        setOf(
            R.id.mainFragment,
            R.id.collectionsFragment,
            R.id.favoritesFragment,
            R.id.settings
        )
    )

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
        navigationBarCorners()
        getLanguage()
        setTheme()
    }

    private fun getLanguage() {
        viewModel.languageState.observeInLifecycle(lifecycleOwner = this, observer = { lang ->
            setLocale(lang?.value)
            updateBottomNavigationTitles(lang?.value)
        })
    }

    private fun setLocale(language: String?) {
        val locale = language?.let { Locale(it) }
        if (locale != null) {
            Locale.setDefault(locale)
        }
        val config = Configuration()
        config.setLocale(locale)
        this.resources.updateConfiguration(
            config,
            this.resources.displayMetrics
        )
        language?.let { LocaleHelper.setLocale(context = this@MainActivity, it) }
    }

    private fun setTheme() {
        viewModel.themeState.observeInLifecycle(this, observer = { theme ->
            if (theme != null) {
                when (theme.value) {
                    ThemeValues.LIGHT_MODE.title -> {
                        AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_NO
                        )
                    }

                    ThemeValues.DARK_MODE.title -> {
                        AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_YES
                        )
                    }

                    ThemeValues.SYSTEM_DEFAULT.title -> {
                        AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                        )
                    }
                }
            }
        })
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment_content_main
        ) as NavHostFragment
        navController = navHostFragment.navController
        navController.navInflater.inflate(R.navigation.graph_wallies)
        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            viewModel.showBottomNavigation.value = when (destination.id) {
                R.id.mainFragment,
                R.id.collectionsFragment,
                R.id.favoritesFragment,
                R.id.settings
                -> true

                else -> false
            }
            if (viewModel.showBottomNavigation.value == true) {
                binding.bottomNavigationView.show()
            } else {
                binding.bottomNavigationView.hide()
            }
        }
    }

    private fun updateBottomNavigationTitles(selectedLanguage: String?) {
        val bottomNavigationView = binding.bottomNavigationView
        when (selectedLanguage) {
            LanguageValues.English.title -> {
                bottomNavigationView.menu.getItem(0).setTitle(
                    R.string.wallpapers_title
                )
                bottomNavigationView.menu.getItem(1).setTitle(
                    R.string.collections_title
                )
                bottomNavigationView.menu.getItem(2).setTitle(
                    R.string.favorites_title
                )
                bottomNavigationView.menu.getItem(3).setTitle(
                    R.string.settings_title
                )
            }

            LanguageValues.Turkish.title -> {
                bottomNavigationView.menu.getItem(0).setTitle(
                    R.string.wallpapers_title
                )
                bottomNavigationView.menu.getItem(1).setTitle(
                    R.string.collections_title
                )
                bottomNavigationView.menu.getItem(2).setTitle(
                    R.string.favorites_title
                )
                bottomNavigationView.menu.getItem(3).setTitle(
                    R.string.settings_title
                )
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_content_main).navigateUp(
            appBarConfiguration
        )
    }

    private fun navigationBarCorners() {
        val radius = resources.getDimension(R.dimen.dp_8)
        val bottomNavigationViewBackground =
            binding.bottomNavigationView.background as MaterialShapeDrawable
        bottomNavigationViewBackground.shapeAppearanceModel =
            bottomNavigationViewBackground.shapeAppearanceModel.toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, radius)
                .setTopLeftCorner(CornerFamily.ROUNDED, radius).build()
    }

    fun slideUp() {
        binding.bottomNavigationView.show()
    }

    fun slideDown() {
        binding.bottomNavigationView.hide()
    }
}
