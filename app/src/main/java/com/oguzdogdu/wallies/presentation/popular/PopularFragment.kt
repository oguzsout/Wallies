package com.oguzdogdu.wallies.presentation.popular

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.oguzdogdu.domain.Resource
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentPopularBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
@AndroidEntryPoint
class PopularFragment : BaseFragment<FragmentPopularBinding>(FragmentPopularBinding::inflate) {
    private val viewModel: PopularViewModel by viewModels()
    private val mainWallpaperAdapter = MainWallpaperAdapter()
    override fun initViews() {
        super.initViews()
        binding.apply {
            recyclerViewWallpapers.adapter = mainWallpaperAdapter
            val layoutManager = GridLayoutManager(requireContext(), 2)
            recyclerViewWallpapers.layoutManager = layoutManager
            recyclerViewWallpapers.setHasFixedSize(true)
        }
    }

    override fun observeData() {
        super.observeData()
        lifecycleScope.launchWhenStarted {
            viewModel.getPopular.collectLatest { result ->
                when {
                    result.isLoading -> {
                        binding.shimmerLayout.startShimmer()
                    }
                    result.error.isNotEmpty() -> {

                    }
                    result.popular.isNotEmpty() -> {
                        with(binding){
                            shimmerLayout.apply {
                                visibility = View.GONE
                                stopShimmer()
                                mainWallpaperAdapter.submitList(result.popular)
                                recyclerViewWallpapers.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }
    }
}