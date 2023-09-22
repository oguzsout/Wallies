package com.oguzdogdu.wallieshd.presentation.splash

import androidx.fragment.app.viewModels
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.databinding.FragmentSplashBinding
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private val viewModel: SplashViewModel by viewModels()

    override fun observeData() {
        super.observeData()
        checkUserSignInStatus()
    }

    private fun checkUserSignInStatus() {
        viewModel.splashState.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            delay(2000)
            when (state) {
                is SplashScreenState.UserSignedIn -> navigateWithDirection(
                    SplashFragmentDirections.toMain()
                )

                is SplashScreenState.UserNotSigned -> navigateWithDirection(
                    SplashFragmentDirections.toLogin()
                )

                else -> {}
            }
        })
    }
}
