package com.oguzdogdu.wallieshd.presentation.topics.topicdetaillist

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.core.snackbar.MessageType
import com.oguzdogdu.wallieshd.databinding.FragmentTopicDetailListBinding
import com.oguzdogdu.wallieshd.presentation.main.MainActivity
import com.oguzdogdu.wallieshd.util.LoaderAdapter
import com.oguzdogdu.wallieshd.util.hide
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.setupRecyclerView
import com.oguzdogdu.wallieshd.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopicDetailListFragment :
    BaseFragment<FragmentTopicDetailListBinding>(FragmentTopicDetailListBinding::inflate) {

    private val viewModel: TopicDetailListViewModel by viewModels()

    private val topicDetailListAdapter by lazy { TopicDetailListAdapter() }

    private val args: TopicDetailListFragmentArgs by navArgs()

    override fun firstExecution(savedInstanceState: Bundle?) {
        super.firstExecution(savedInstanceState)
        viewModel.handleUIEvent(TopicDetailListEvent.FetchTopicListData(args.idOrSlug))
    }

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        binding.apply {
            args.idOrSlug?.let {
                toolbar.setTitle(
                    title = it,
                    titleStyleRes = R.style.DialogTitleText
                )
            }
            toolbar.setLeftIcon(R.drawable.back)
            recyclerViewTopicDetailList.setupRecyclerView(
                layout = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL),
                adapter = topicDetailListAdapter.withLoadStateFooter(LoaderAdapter()),
                true
            ) {
                recyclerViewTopicDetailList.addOnScrollListener(object :
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
            viewModel.handleUIEvent(TopicDetailListEvent.FetchTopicListData(args.idOrSlug))
            binding.swipeRefresh.isRefreshing = false
        }
        topicDetailListAdapter.setOnItemClickListener {
            navigateWithDirection(TopicDetailListFragmentDirections.toDetail(it?.id))
        }
        binding.toolbar.setLeftIconClickListener {
            navigateBack()
        }
    }

    override fun observeData() {
        super.observeData()
        fetchTopicDetailData()
        handlePagingState()
    }

    private fun fetchTopicDetailData() {
        viewModel.getTopicList.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when (state) {
                is TopicDetailListState.TopicListState -> {
                    state.topicList?.let { topicDetailListAdapter.submitData(it) }
                }

                null -> {
                }
            }
        })
    }

    private fun handlePagingState() {
        topicDetailListAdapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    binding.progressBar.show()
                    binding.recyclerViewTopicDetailList.hide()
                }
                is LoadState.NotLoading -> {
                    binding.progressBar.hide()
                    binding.recyclerViewTopicDetailList.show()
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
