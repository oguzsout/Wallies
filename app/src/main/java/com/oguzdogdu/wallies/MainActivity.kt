package com.oguzdogdu.wallies

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.oguzdogdu.wallies.databinding.ActivityMainBinding
import com.oguzdogdu.wallies.presentation.main.MainViewModel
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

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment_content_main
        ) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // setup bottom bar visibility

            viewModel.showBottomNavigation.value = when (destination.id) {
                R.id.mainFragment,
                R.id.collectionsFragment,
                R.id.favoritesFragment,
                R.id.settingsFragment -> true
                else -> false
            }
            if (viewModel.showBottomNavigation.value == true){
                binding.bottomNavContainer.show()
            } else {
                binding.bottomNavContainer.hide()
            }
        }
    }

    private fun navigationBarCorners(){
        val radius = resources.getDimension(R.dimen.dp_8)
        val bottomNavigationViewBackground = binding.bottomNavigationView.background as MaterialShapeDrawable
        bottomNavigationViewBackground.shapeAppearanceModel =
            bottomNavigationViewBackground.shapeAppearanceModel.toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, radius)
                .setTopLeftCorner(CornerFamily.ROUNDED, radius)
                .build()
    }
}
