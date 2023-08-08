package com.oguzdogdu.wallies.presentation.latest

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentLatestBinding
import com.oguzdogdu.wallies.presentation.main.MainActivity
import com.oguzdogdu.wallies.util.hide
import com.oguzdogdu.wallies.util.setupRecyclerView
import com.oguzdogdu.wallies.util.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LatestFragment : BaseFragment<FragmentLatestBinding>(FragmentLatestBinding::inflate) {

    private val viewModel: LatestViewModel by viewModels()

    private val latestWallpaperAdapter by lazy { LatestWallpaperAdapter() }

    override fun initViews() {
        super.initViews()
        binding.apply {
            recyclerViewWallpapers.setupRecyclerView(
                layout = GridLayoutManager(requireContext(), 3),
                adapter = latestWallpaperAdapter,
                true
            ) {
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
        }
    }

    override fun initListeners() {
        super.initListeners()
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getLatestImages()
            binding.swipeRefresh.isRefreshing = false
        }
        latestWallpaperAdapter.setOnItemClickListener {
            navigateWithDirection(LatestFragmentDirections.toDetail(id = it?.id))
        }
    }

    override fun observeData() {
        super.observeData()
        checkConnection()
    }

    private fun checkConnection() {
        viewModel.getLatestImages()
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getLatest.collect {
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
}
