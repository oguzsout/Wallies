package com.oguzdogdu.wallies.presentation.latest

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentLatestBinding
import com.oguzdogdu.wallies.util.hide
import com.oguzdogdu.wallies.util.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@AndroidEntryPoint
class LatestFragment : BaseFragment<FragmentLatestBinding>(FragmentLatestBinding::inflate) {

    private val viewModel: LatestViewModel by viewModels()
     private val latestWallpaperAdapter = LatestWallpaperAdapter()

    override fun initViews() {
        super.initViews()
        binding.apply {
            recyclerViewWallpapers.adapter = latestWallpaperAdapter
            val staggeredGridLayoutManager =
                StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            recyclerViewWallpapers.layoutManager = staggeredGridLayoutManager
            recyclerViewWallpapers.setHasFixedSize(true)
        }
    }

    override fun observeData() {
        super.observeData()
        lifecycleScope.launchWhenStarted {
            viewModel.getLatest.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).distinctUntilChanged().collectLatest { result ->
                when {
                    result.isLoading -> {
                        binding.progressBar.show()
                    }
                    result.error.isNotEmpty() -> {

                    }
                    result.latest.isNotEmpty() -> {
                        binding.progressBar.hide()
                        latestWallpaperAdapter.submitList(result.latest)
                    }
                }
            }
        }
    }
}