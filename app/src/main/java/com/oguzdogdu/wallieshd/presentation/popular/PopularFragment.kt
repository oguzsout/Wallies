package com.oguzdogdu.wallieshd.presentation.popular

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.core.snackbar.MessageType
import com.oguzdogdu.wallieshd.databinding.FragmentPopularBinding
import com.oguzdogdu.wallieshd.presentation.main.MainActivity
import com.oguzdogdu.wallieshd.util.LoaderAdapter
import com.oguzdogdu.wallieshd.util.hide
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.setupRecyclerView
import com.oguzdogdu.wallieshd.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularFragment : BaseFragment<FragmentPopularBinding>(FragmentPopularBinding::inflate) {

    private val viewModel: PopularViewModel by viewModels()

    private val popularWallpaperAdapter by lazy { PopularWallpaperAdapter() }

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        binding.apply {
            toolbar.setTitle(
                title = getString(R.string.popular_title),
                titleStyleRes = R.style.DialogTitleText
            )
            toolbar.setLeftIcon(R.drawable.back)
            recyclerViewWallpapers.setupRecyclerView(
                layout = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL),
                adapter = popularWallpaperAdapter.withLoadStateHeaderAndFooter(
                    LoaderAdapter(),
                    LoaderAdapter()
                ),
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
            viewModel.handleUIEvent(PopularScreenEvent.FetchPopularData)
            binding.swipeRefresh.isRefreshing = false
        }
        binding.toolbar.setLeftIconClickListener {
            navigateBack()
        }
    }

    override fun observeData() {
        super.observeData()
        fetchPopularData()
        handlePagingState()
    }

    private fun fetchPopularData() {
        viewModel.getPopular.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when (state) {
                is PopularState.ItemState -> {
                    popularWallpaperAdapter.submitData(state.popular)
                }
            }
        })
    }

    private fun handlePagingState() {
        popularWallpaperAdapter.addLoadStateListener { loadState ->
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
                    showMessage(message = message, type = MessageType.ERROR)
                }
            }
        }
    }
}
