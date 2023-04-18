package com.oguzdogdu.wallies.presentation.popular

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentPopularBinding
import com.oguzdogdu.wallies.util.*
import dagger.hilt.android.AndroidEntryPoint
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
        checkConnection()
        viewModel.getPopularImages()
        getImages()
    }

    private fun checkConnection(){
        connection.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected == true) {

            } else {
                requireView().showToast(requireContext(),R.string.internet_error)
            }
        }
    }

    private fun getImages(){
        lifecycleScope.launch {
            viewModel.getPopular.onEach { result ->
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
            }.observeInLifecycle(this@PopularFragment)
        }
    }
}