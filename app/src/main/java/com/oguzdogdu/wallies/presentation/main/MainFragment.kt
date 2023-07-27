package com.oguzdogdu.wallies.presentation.main

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.google.android.material.tabs.TabLayoutMediator
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentMainBinding
import com.oguzdogdu.wallies.presentation.latest.LatestFragment
import com.oguzdogdu.wallies.presentation.popular.PopularFragment
import com.oguzdogdu.wallies.util.observeInLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val fragments =
        listOf(PopularFragment(), LatestFragment())

    private val viewModel: MainViewModel by viewModels()

    private val tabTitles = listOf("POPULAR", "LATEST")
    override fun initViews() {
        super.initViews()
        initViewPager()
        initTabLayout()
    }

    override fun observeData() {
        super.observeData()
        getAuthenticatedUserInfos()
    }

    private fun getAuthenticatedUserInfos() {
        lifecycleScope.launch {
            viewModel.userState.observeInLifecycle(viewLifecycleOwner, observer = { data ->
                if (data != null) {
                    if (data.profileImage != null) {
                        binding.imageViewProfileAvatar.load(data.profileImage) {
                            diskCachePolicy(CachePolicy.DISABLED)
                            transformations(CircleCropTransformation())
                            allowConversionToBitmap(true)
                        }
                    } else {
                        binding.imageViewProfileAvatar.load(R.drawable.ic_default_avatar)
                    }
                }
            })
        }
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
