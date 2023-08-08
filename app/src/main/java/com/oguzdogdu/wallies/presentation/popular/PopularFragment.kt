package com.oguzdogdu.wallies.presentation.popular

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentPopularBinding
import com.oguzdogdu.wallies.presentation.main.MainActivity
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.setupRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PopularFragment : BaseFragment<FragmentPopularBinding>(FragmentPopularBinding::inflate) {

    private val viewModel: PopularViewModel by viewModels()

    private val popularWallpaperAdapter by lazy { PopularWallpaperAdapter() }

    override fun initViews() {
        super.initViews()
        binding.apply {
            recyclerViewWallpapers.setupRecyclerView(
                layout = GridLayoutManager(requireContext(), 3),
                adapter = popularWallpaperAdapter,
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
        popularWallpaperAdapter.setOnItemClickListener {
            navigateWithDirection(PopularFragmentDirections.toDetail(id = it?.id))
        }
        binding.swipeRefresh.setOnRefreshListener {
            checkConnection()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun observeData() {
        super.observeData()
        checkConnection()
    }

    private fun checkConnection() {
        viewModel.getPopularImages()
        viewModel.getPopular.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when {
                state.error.isNotEmpty() -> {}

                else -> {
                    popularWallpaperAdapter.submitData(state.popular)
                    popularWallpaperAdapter.loadStateFlow.collectLatest { loadStates ->
                        binding.progressBar.isVisible = loadStates.refresh is LoadState.Loading
                    }
                }
            }
        })
    }
}
