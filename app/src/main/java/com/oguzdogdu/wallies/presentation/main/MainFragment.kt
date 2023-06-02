package com.oguzdogdu.wallies.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentMainBinding
import com.oguzdogdu.wallies.presentation.latest.LatestFragment
import com.oguzdogdu.wallies.presentation.popular.PopularFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val fragments =
        listOf(PopularFragment(), LatestFragment())

    private val tabTitles = listOf("POPULAR", "LATEST")
    override fun initViews() {
        super.initViews()
        initViewPager()
        initTabLayout()
    }

    override fun initListeners() {
        super.initListeners()
        binding.imageViewSearchWalpapers.setOnClickListener {
            navigate(R.id.toSearch, null)
        }
    }

    private fun initTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }


    private fun initViewPager() {
        val pagerAdapter = ViewPagerAdapter(requireParentFragment().requireActivity(), fragments)
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.isUserInputEnabled = false
    }
}