package com.oguzdogdu.wallies.presentation.latest

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentLatestBinding
import com.oguzdogdu.wallies.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LatestFragment : BaseFragment<FragmentLatestBinding>(FragmentLatestBinding::inflate) {

    @Inject
    lateinit var connection : CheckConnection

    private val viewModel: LatestViewModel by viewModels()

    private val latestWallpaperAdapter = LatestWallpaperAdapter()

    override fun initViews() {
        super.initViews()
        binding.apply {
            recyclerViewWallpapers.adapter = latestWallpaperAdapter
            val layoutManager = GridLayoutManager(requireContext(), 2)
            recyclerViewWallpapers.layoutManager = layoutManager
            recyclerViewWallpapers.setHasFixedSize(true)
        }
    }

    override fun initListeners() {
        super.initListeners()
        latestWallpaperAdapter.setOnItemClickListener {
            val arguments = Bundle().apply {
                putString("id", it?.id)
            }
            navigate(R.id.toDetail,arguments)
        }
    }

    override fun observeData() {
        super.observeData()
        lifecycleScope.launch {
            viewModel.getLatest.onEach { result ->
                when {
                    result.isLoading -> {
                        binding.progressBar.show()
                    }
                    result.error.isNotEmpty() -> {
                        connection.observe(this@LatestFragment) { isConnected ->
                            if (isConnected) {
                                viewModel.getLatestImages()
                            } else {
                                requireView().showSnackMessage("Check Connectivity")
                            }
                        }
                    }
                    result.latest.isNotEmpty() -> {
                        binding.progressBar.hide()
                        latestWallpaperAdapter.submitList(result.latest)
                    }
                }
            }.observeInLifecycle(this@LatestFragment)
        }
    }
}