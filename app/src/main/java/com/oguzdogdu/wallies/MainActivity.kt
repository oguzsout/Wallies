package com.oguzdogdu.wallies

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.oguzdogdu.wallies.databinding.ActivityMainBinding
import com.oguzdogdu.wallies.util.CheckConnection
import com.oguzdogdu.wallies.util.DialogHelper
import dagger.hilt.android.AndroidEntryPoint


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
