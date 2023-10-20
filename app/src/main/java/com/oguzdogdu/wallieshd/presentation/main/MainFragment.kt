package com.oguzdogdu.wallieshd.presentation.main

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
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
        viewModel.handleUIEvent(MainScreenEvent.FetchMainScreenUserData)
        getAuthenticatedUserInfos()
    }

    private fun getAuthenticatedUserInfos() {
        viewModel.userState.observeInLifecycle(viewLifecycleOwner, observer = { status ->
            when (status) {
                is MainScreenState.UserProfile -> {
                    when (status.profileImage.isNullOrBlank()) {
                        true -> binding.imageViewProfileAvatar.load(R.drawable.ic_default_avatar)

                        false -> binding.imageViewProfileAvatar.load(status.profileImage) {
                            diskCachePolicy(CachePolicy.DISABLED)
                            transformations(CircleCropTransformation())
                        }
                    }
                }

                is MainScreenState.UserAuthenticated -> {
                    when (status.isAuthenticated) {
                        false -> binding.imageViewProfileAvatar.load(R.drawable.ic_default_avatar)
                        else -> {}
                    }
                    status.isAuthenticated?.let { goToUserInfoScreen(it) }
                }

                else -> {}
            }
        })
    }

    override fun initListeners() {
        super.initListeners()
        binding.imageViewSearchWalpapers.setOnClickListener {
            navigate(R.id.toSearch, null)
        }
    }

    private fun goToUserInfoScreen(isAuthenticated: Boolean) {
        binding.imageViewProfileAvatar.setOnClickListener {
            navigate(R.id.toAuthUser, bundleOf(Pair("auth", isAuthenticated)))
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
