package com.oguzdogdu.wallies.presentation.unsplashprofile.webProfile

import android.annotation.SuppressLint
import androidx.navigation.fragment.navArgs
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentUnsplashProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UnsplashProfileFragment :
    BaseFragment<FragmentUnsplashProfileBinding>(FragmentUnsplashProfileBinding::inflate) {

    private val args: UnsplashProfileFragmentArgs by navArgs()

    @SuppressLint("SetJavaScriptEnabled")
    override fun initViews() {
        super.initViews()
        binding.apply {
            webView.settings.apply {
                javaScriptEnabled = true
                setSupportZoom(true)
            }
            webView.loadUrl(args.url ?: "")
        }
    }
}
