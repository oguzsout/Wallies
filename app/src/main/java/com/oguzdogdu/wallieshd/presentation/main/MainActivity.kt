package com.oguzdogdu.wallieshd.presentation.main

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.databinding.ActivityMainBinding
import com.oguzdogdu.wallieshd.presentation.settings.LanguageValues
import com.oguzdogdu.wallieshd.presentation.settings.SettingsEvent
import com.oguzdogdu.wallieshd.presentation.settings.SettingsViewModel
import com.oguzdogdu.wallieshd.presentation.settings.ThemeValues
import com.oguzdogdu.wallieshd.util.LocaleHelper
import com.oguzdogdu.wallieshd.util.hide
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private lateinit var appUpdateManager: AppUpdateManager

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
        appUpdateManager = AppUpdateManagerFactory.create(this)
        checkUpdates()
        setupNavigation()
        navigationBarCorners()
        getLanguage()
        setTheme()
    }

    private fun checkUpdates() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(
                    AppUpdateType.FLEXIBLE
                )
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    activityResultLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
                )
            }
        }
        appUpdateManager.registerListener(listener)
    }

    private val listener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            Toast.makeText(this, "Download Successful", Toast.LENGTH_LONG)
        }
    }
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode != RESULT_OK) {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG)
            }
        }

    private fun getLanguage() {
        viewModel.handleUIEvent(SettingsEvent.LanguageChanged)
        viewModel.languageState.observeInLifecycle(lifecycleOwner = this, observer = { lang ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                LocaleHelper(context = this).updateResourcesLegacy(lang.value)
            } else {
                LocaleHelper(context = this).updateResources(lang.value)
            }

            updateBottomNavigationTitles(lang.value)
        })
    }

    private fun setTheme() {
        viewModel.handleUIEvent(SettingsEvent.ThemeChanged)
        viewModel.themeState.observeInLifecycle(this, observer = { theme ->
            when (theme?.value) {
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
        })
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment_content_main
        ) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            viewModel.showBottomNavigation.value = when (destination.id) {
                R.id.mainFragment, R.id.collectionsFragment, R.id.favoritesFragment, R.id.settings -> true

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

    override fun onDestroy() {
        super.onDestroy()
        appUpdateManager.unregisterListener(listener)
    }
}
