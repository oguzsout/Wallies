package com.oguzdogdu.wallieshd.presentation.topics

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.core.snackbar.MessageType
import com.oguzdogdu.wallieshd.databinding.FragmentTopicsListBinding
import com.oguzdogdu.wallieshd.presentation.main.MainActivity
import com.oguzdogdu.wallieshd.util.LoaderAdapter
import com.oguzdogdu.wallieshd.util.hide
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.setupRecyclerView
import com.oguzdogdu.wallieshd.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopicsListFragment :
    BaseFragment<FragmentTopicsListBinding>(FragmentTopicsListBinding::inflate) {

    private val viewModel: TopicsViewModel by viewModels()

    private val topicsListAdapter by lazy { TopicsListAdapter() }

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        binding.apply {
            toolbar.setTitle(
                title = getString(R.string.topics_title),
                titleStyleRes = R.style.DialogTitleText
            )
            toolbar.setLeftIcon(R.drawable.back)
            recyclerViewTopics.setupRecyclerView(
                layout = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL),
                adapter = topicsListAdapter.withLoadStateFooter(LoaderAdapter()),
                true
            ) {
                recyclerViewTopics.addOnScrollListener(object :
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
            viewModel.handleUIEvent(TopicsScreenEvent.FetchTopicsData)
            binding.swipeRefresh.isRefreshing = false
        }
        binding.toolbar.setLeftIconClickListener {
            navigateBack()
        }
    }

    override fun observeData() {
        super.observeData()
        fetchLatestData()
        handlePagingState()
    }

    private fun fetchLatestData() {
        viewModel.getTopics.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when (state) {
                is TopicsState.TopicsListState -> state.topics.let {
                    topicsListAdapter.submitData(
                        it
                    )
                }

                null -> {
                }
            }
        })
    }

    private fun handlePagingState() {
        topicsListAdapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    binding.progressBar.show()
                    binding.recyclerViewTopics.hide()
                }
                is LoadState.NotLoading -> {
                    binding.progressBar.hide()
                    binding.recyclerViewTopics.show()
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
