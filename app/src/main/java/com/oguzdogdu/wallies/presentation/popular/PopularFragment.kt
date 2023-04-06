package com.oguzdogdu.wallies.presentation.popular

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentPopularBinding
import com.oguzdogdu.wallies.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PopularFragment : BaseFragment<FragmentPopularBinding>(FragmentPopularBinding::inflate) {

    @Inject
    lateinit var connection : CheckConnection

    private val viewModel: PopularViewModel by viewModels()

    private val popularWallpaperAdapter by lazy { PopularWallpaperAdapter() }

    override fun initViews() {
        super.initViews()
        binding.apply {
            recyclerViewWallpapers.adapter = popularWallpaperAdapter
            val layoutManager = GridLayoutManager(requireContext(), 2)
            recyclerViewWallpapers.layoutManager = layoutManager
            recyclerViewWallpapers.setHasFixedSize(true)
        }
    }

    override fun initListeners() {
        super.initListeners()
        popularWallpaperAdapter.setOnItemClickListener {
            val arguments = Bundle().apply {
                putString("id", it?.id)
            }
            navigate(R.id.toDetail, arguments)
        }
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getPopularImages()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.getPopularImages()
        getImages()
    }

    private fun getImages(){
        lifecycleScope.launch {
            viewModel.getPopular.onEach { result ->
                when {
                    result.isLoading -> {
                        binding.progressBar.show()
                    }
                    result.error.isNotEmpty() -> {
                        connection.observe(this@PopularFragment) { isConnected ->
                            if (isConnected) {

                            } else {
                                DialogHelper.showInternetCheckDialog(requireContext()){
                                    viewModel.getPopularImages()
                                }
                            }
                        }
                    }
                    result.popular.isNotEmpty() -> {
                        binding.progressBar.hide()
                        popularWallpaperAdapter.submitList(result.popular)
                    }
                }
            }.observeInLifecycle(this@PopularFragment)
        }
    }
}