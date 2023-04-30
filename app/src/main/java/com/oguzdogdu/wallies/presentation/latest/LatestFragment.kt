package com.oguzdogdu.wallies.presentation.latest

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentLatestBinding
import com.oguzdogdu.wallies.presentation.main.MainActivity
import com.oguzdogdu.wallies.util.CheckConnection
import com.oguzdogdu.wallies.util.hide
import com.oguzdogdu.wallies.util.observe
import com.oguzdogdu.wallies.util.setUp
import com.oguzdogdu.wallies.util.show
import com.oguzdogdu.wallies.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LatestFragment : BaseFragment<FragmentLatestBinding>(FragmentLatestBinding::inflate) {

    @Inject
    lateinit var connection: CheckConnection

    private val viewModel: LatestViewModel by viewModels()

    private val latestWallpaperAdapter by lazy { LatestWallpaperAdapter() }

    override fun initViews() {
        super.initViews()
        binding.apply {
            recyclerViewWallpapers.setUp(
                layoutManager = GridLayoutManager(requireContext(), 2),
                adapter = latestWallpaperAdapter,
                true,
                onScroll = {
                    recyclerViewWallpapers.addOnScrollListener(object :
                        RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            if (dy > 0) {
                                (activity as MainActivity).slideDown()
                            } else if (dy < 0) {
                                (activity as MainActivity).slideUp()
                            }
                        }
                    })
                }
            )
        }
    }

    override fun initListeners() {
        super.initListeners()
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getLatestImages()
            binding.swipeRefresh.isRefreshing = false
        }
        latestWallpaperAdapter.setOnItemClickListener {
            val arguments = Bundle().apply {
                putString("id", it?.id)
            }
            navigate(R.id.toDetail, arguments)
        }
    }

    override fun observeData() {
        super.observeData()
        checkConnection()
    }

    private fun checkConnection() {
        connection.observe(this@LatestFragment) { isConnected ->
            when (isConnected) {
                true -> {
                    viewModel.getLatestImages()
                    observe(viewModel.getLatest, viewLifecycleOwner) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            when {
                                it.isLoading -> {
                                    binding.progressBar.show()
                                }

                                it.error.isNotEmpty() -> {}

                                else -> {
                                    binding.progressBar.hide()
                                    latestWallpaperAdapter.submitData(it.latest)
                                }
                            }
                        }

                    }
                }

                false -> {
                    requireView().showToast(requireContext(), R.string.internet_error)
                }

                null -> {}
            }
        }
    }
}