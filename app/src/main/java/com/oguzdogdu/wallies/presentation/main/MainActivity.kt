package com.oguzdogdu.wallies.presentation.main

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.databinding.ActivityMainBinding
import com.oguzdogdu.wallies.presentation.settings.LanguageValues
import com.oguzdogdu.wallies.presentation.settings.SettingsViewModel
import com.oguzdogdu.wallies.presentation.settings.ThemeValues
import com.oguzdogdu.wallies.util.LocaleHelper
import com.oguzdogdu.wallies.util.hide
import com.oguzdogdu.wallies.util.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val viewModel: SettingsViewModel by viewModels()

    private var isStartDestinationChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getLanguage()
        setTheme()
        setupNavigation()
        navigationBarCorners()
    }

    private fun getLanguage() {
        lifecycleScope.launch {
            viewModel.languageState.collectLatest { lang ->
                if (lang != null) {
                    when (lang.value) {
                        LanguageValues.English.title -> {
                            setLocale(LanguageValues.English.title)
                        }

                        LanguageValues.Turkish.title -> {
                            setLocale(LanguageValues.Turkish.title)
                        }
                    }
                }
            }
        }
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
        LocaleHelper.setLocale(context = this@MainActivity, language.orEmpty())
    }

    private fun setTheme() {
        lifecycleScope.launch {
            viewModel.themeState.collectLatest { theme ->
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
            }
        }
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment_content_main
        ) as NavHostFragment
        navController = navHostFragment.navController
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
            if (destination.id != R.id.splashFragment && !isStartDestinationChanged) {
                val navGraph = navController.graph
                navGraph.setStartDestination(R.id.mainFragment)
                navGraph.startDestinationId
                isStartDestinationChanged = true
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
                .setTopLeftCorner(CornerFamily.ROUNDED, radius)
                .build()
    }

    fun slideUp() {
        binding.bottomNavigationView.show()
    }

    fun slideDown() {
        binding.bottomNavigationView.hide()
    }
}
