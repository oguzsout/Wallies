package com.oguzdogdu.wallies.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.oguzdogdu.wallies.MainActivity
import com.oguzdogdu.wallies.databinding.ActivityMainBinding
import com.oguzdogdu.wallies.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launchWhenCreated {
            goToMainFragment()
        }
    }

    private suspend fun goToMainFragment() {
        delay(3000)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}