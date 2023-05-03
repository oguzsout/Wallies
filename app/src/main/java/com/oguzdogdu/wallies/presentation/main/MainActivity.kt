package com.oguzdogdu.wallies.presentation.main

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.databinding.ActivityMainBinding
import com.oguzdogdu.wallies.util.LocaleHelper
import com.oguzdogdu.wallies.util.ThemeKeys
import com.oguzdogdu.wallies.util.hide
import com.oguzdogdu.wallies.util.show
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var navController: NavController

    private val viewModel: MainViewModel by viewModels()

    private var isStartDestinationChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTheme()
        setupNavigation()
        navigationBarCorners()
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
                R.id.settingsFragment,
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
