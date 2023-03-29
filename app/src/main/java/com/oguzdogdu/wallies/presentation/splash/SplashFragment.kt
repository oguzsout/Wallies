package com.oguzdogdu.wallies.presentation.splash

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {
    override fun initViews() {
        super.initViews()
        lifecycleScope.launchWhenCreated {
            goToMainFragment()
        }
    }
    private suspend fun goToMainFragment() {
        delay(3000)
       findNavController().navigate(SplashFragmentDirections.toMain())
    }
}