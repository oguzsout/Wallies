package com.oguzdogdu.wallies

import android.annotation.SuppressLint
import android.app.PendingIntent.getActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.enableSavedStateHandles
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavAction
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.oguzdogdu.wallies.databinding.ActivityMainBinding
import com.oguzdogdu.wallies.presentation.main.MainFragment
import com.oguzdogdu.wallies.presentation.main.MainFragmentDirections
import com.oguzdogdu.wallies.presentation.popular.PopularFragment
import com.oguzdogdu.wallies.util.CheckConnection
import com.oguzdogdu.wallies.util.DialogHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkConnection()
    }

    @SuppressLint("CommitTransaction")
    private fun checkConnection() {
        lifecycleScope.launchWhenCreated {
            CheckConnection.init(this@MainActivity)
            CheckConnection.observe(this@MainActivity) { isConnected ->
                if (isConnected) {
                    val navHostFragment =
                        supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
                    navHostFragment.navController.navigate(R.id.mainFragment)
                } else {
                    DialogHelper.showInternetCheckDialog(this@MainActivity) {
                    }
                    Snackbar.make(
                        this@MainActivity.binding.root,
                        "Check Connectivity",
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                }

            }
        }
    }
}
