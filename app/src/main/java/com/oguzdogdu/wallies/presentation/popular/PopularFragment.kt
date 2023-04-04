package com.oguzdogdu.wallies.presentation.popular

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentPopularBinding
import com.oguzdogdu.wallies.util.addItemDivider
import com.oguzdogdu.wallies.util.hide
import com.oguzdogdu.wallies.util.show
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
            swipeRefresh.setOnRefreshListener {
                getImages()
                swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun observeData() {
        super.observeData()
        getImages()
    }

    private fun getImages(){
        lifecycleScope.launchWhenStarted {
            viewModel.getPopular.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).distinctUntilChanged().collectLatest { result ->
                when {
                    result.isLoading -> {
                        binding.progressBar.show()
                    }
                    result.error.isNotEmpty() -> {

                    }
                    result.popular.isNotEmpty() -> {
                        binding.progressBar.hide()
                        popularWallpaperAdapter.submitList(result.popular)
                    }
                }
            }
        }
    }
}