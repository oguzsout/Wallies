package com.oguzdogdu.wallies.presentation.collections

import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.core.snackbar.MessageType
import com.oguzdogdu.wallies.databinding.FragmentCollectionsBinding
import com.oguzdogdu.wallies.presentation.main.MainActivity
import com.oguzdogdu.wallies.util.LoaderAdapter
import com.oguzdogdu.wallies.util.hide
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.setupRecyclerView
import com.oguzdogdu.wallies.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionsFragment :
    BaseFragment<FragmentCollectionsBinding>(FragmentCollectionsBinding::inflate) {

    private val viewModel: CollectionViewModel by viewModels()

    private val collectionListAdapter by lazy { CollectionListAdapter() }

    override fun initViews() {
        super.initViews()
        with(binding) {
            recyclerViewCollections.setupRecyclerView(
                layout = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL),
                adapter = collectionListAdapter.withLoadStateFooter(LoaderAdapter()),
                true
            ) {
                recyclerViewCollections.addOnScrollListener(object :
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
            toolbarCollection.setTitle(
                title = getString(R.string.collections_screen_title_text),
                titleStyleRes = R.style.ToolbarTitleText
            )
        }
    }

    override fun initListeners() {
        super.initListeners()
        collectionListAdapter.setOnItemClickListener {
            navigateWithDirection(
                CollectionsFragmentDirections.toCollectionsLists(
                    id = it?.id,
                    title = it?.title
                )
            )
        }
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.handleUIEvent(CollectionScreenEvent.FetchLatestData)
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun observeData() {
        super.observeData()
        showCollectionsDatas()
        handlePagingState()
    }

    private fun showCollectionsDatas() {
        viewModel.getCollections.observeInLifecycle(
            viewLifecycleOwner,
            observer = { state -> state?.let { collectionListAdapter.submitData(it.collections) } }
        )
    }

    private fun handlePagingState() {
        collectionListAdapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    binding.progressBar.show()
                    binding.recyclerViewCollections.hide()
                }
                is LoadState.NotLoading -> {
                    binding.progressBar.hide()
                    binding.recyclerViewCollections.show()
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
