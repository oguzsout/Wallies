package com.oguzdogdu.wallieshd.presentation.main

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.google.android.material.tabs.TabLayoutMediator
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.databinding.FragmentMainBinding
import com.oguzdogdu.wallieshd.presentation.latest.LatestFragment
import com.oguzdogdu.wallieshd.presentation.popular.PopularFragment
import com.oguzdogdu.wallieshd.util.observeInLifecycle
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
                when (data?.profileImage.isNullOrBlank()) {
                    true -> binding.imageViewProfileAvatar.load(R.drawable.ic_default_avatar)
                    false -> binding.imageViewProfileAvatar.load(data?.profileImage) {
                        diskCachePolicy(CachePolicy.DISABLED)
                        transformations(CircleCropTransformation())
                        allowConversionToBitmap(true)
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
        binding.imageViewProfileAvatar.setOnClickListener {
            navigateWithDirection(MainFragmentDirections.toAuthUser())
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
