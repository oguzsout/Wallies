package com.oguzdogdu.wallies.presentation.popular

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentPopularBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@AndroidEntryPoint
class PopularFragment : BaseFragment<FragmentPopularBinding>(FragmentPopularBinding::inflate) {
    private val viewModel: PopularViewModel by viewModels()
    private val popularWallpaperAdapter = PopularWallpaperAdapter()
    override fun initViews() {
        super.initViews()
        binding.apply {
            recyclerViewWallpapers.adapter = popularWallpaperAdapter
            val layoutManager = GridLayoutManager(requireContext(), 2)
            recyclerViewWallpapers.layoutManager = layoutManager
            recyclerViewWallpapers.setHasFixedSize(true)
        }
    }

    override fun observeData() {
        super.observeData()
        lifecycleScope.launchWhenStarted {
            viewModel.getPopular.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).distinctUntilChanged().collectLatest { result ->
                when {
                    result.isLoading -> {
                        binding.shimmerLayout.startShimmer()
                    }
                    result.error.isNotEmpty() -> {

                    }
                    result.popular.isNotEmpty() -> {
                        with(binding) {
                            shimmerLayout.apply {
                                visibility = View.GONE
                                stopShimmer()
                                popularWallpaperAdapter.submitList(result.popular)
                                recyclerViewWallpapers.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }
    }
}