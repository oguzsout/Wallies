package com.oguzdogdu.wallies.presentation.latest

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentLatestBinding
import com.oguzdogdu.wallies.presentation.main.MainActivity
import com.oguzdogdu.wallies.util.LoaderAdapter
import com.oguzdogdu.wallies.util.hide
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.setupRecyclerView
import com.oguzdogdu.wallies.util.show
import com.oguzdogdu.wallies.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LatestFragment : BaseFragment<FragmentLatestBinding>(FragmentLatestBinding::inflate) {

    private val viewModel: LatestViewModel by viewModels()

    private val latestWallpaperAdapter by lazy { LatestWallpaperAdapter() }

    override fun initViews() {
        super.initViews()
        binding.apply {
            recyclerViewWallpapers.setupRecyclerView(
                layout = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL),
                adapter = latestWallpaperAdapter.withLoadStateFooter(LoaderAdapter()),
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
            viewModel.handleUIEvent(LatestScreenEvent.FetchLatestData)
            binding.swipeRefresh.isRefreshing = false
        }
        latestWallpaperAdapter.setOnItemClickListener {
            navigateWithDirection(LatestFragmentDirections.toDetail(id = it?.id))
        }
    }

    override fun observeData() {
        super.observeData()
        fetchLatestData()
        handlePagingState()
    }

    private fun fetchLatestData() {
        viewModel.getLatest.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            state?.let { latestWallpaperAdapter.submitData(it.latest) }
        })
    }

    private fun handlePagingState() {
        latestWallpaperAdapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    binding.progressBar.show()
                    binding.recyclerViewWallpapers.hide()
                }
                is LoadState.NotLoading -> {
                    binding.progressBar.hide()
                    binding.recyclerViewWallpapers.show()
                }
                else -> {}
            }
            val errorState = when {
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }
            errorState?.let {
                it.error.message?.let { message ->
                    requireView().showToast(
                        context = requireContext(),
                        message = message,
                        duration = Toast.LENGTH_LONG
                    )
                }
            }
        }
    }
}
