package com.oguzdogdu.wallies.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.oguzdogdu.wallies.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var composeView: ComposeView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        composeView.setContent {
            ContainerScreen(navigateToPopularDetail = {
                val arguments = Bundle().apply {
                    putString("id", it)
                }
                findNavController().navigate(R.id.toDetail, args = arguments)
            }, navigateToLatestDetail = {
                val arguments = Bundle().apply {
                    putString("id", it)
                }
                findNavController().navigate(R.id.toDetail, args = arguments)
            })
        }
    }
}