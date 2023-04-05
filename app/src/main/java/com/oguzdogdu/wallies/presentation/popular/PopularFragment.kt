package com.oguzdogdu.wallies.presentation.popular

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentPopularBinding
import com.oguzdogdu.wallies.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@AndroidEntryPoint
class PopularFragment : BaseFragment<FragmentPopularBinding>(FragmentPopularBinding::inflate) {
    private val viewModel: PopularViewModel by viewModels()
    private val popularWallpaperAdapter by lazy { PopularWallpaperAdapter() }
    override fun initViews() {
        super.initViews()
        binding.apply {
            recyclerViewWallpapers.adapter = popularWallpaperAdapter
            val layoutManager = GridLayoutManager(requireContext(), 2)
            recyclerViewWallpapers.layoutManager = layoutManager
            recyclerViewWallpapers.setHasFixedSize(true)
            popularWallpaperAdapter.setOnItemClickListener {
                val arguments = Bundle().apply {
                    putString("id", it?.id)
                }
                navigate(R.id.toDetail,arguments)
            }
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
        lifecycleScope.launchWhenCreated {
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