package com.oguzdogdu.wallies.presentation.splash

import android.annotation.SuppressLint
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    override fun initViews() {
        super.initViews()
        lifecycleScope.launch {
            goToMainFragment()
        }
    }

    private suspend fun goToMainFragment() {
        delay(2000)
        navigate(R.id.toLogin, null)
        findNavController().clearBackStack(R.id.splashFragment)
    }
}