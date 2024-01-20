package com.oguzdogdu.wallieshd.presentation.search.searchphoto

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.core.snackbar.MessageType
import com.oguzdogdu.wallieshd.databinding.FragmentSearchPhotoBinding
import com.oguzdogdu.wallieshd.presentation.search.SearchEvent
import com.oguzdogdu.wallieshd.presentation.search.SearchState
import com.oguzdogdu.wallieshd.presentation.search.SearchViewModel
import com.oguzdogdu.wallieshd.presentation.search.SearchWallpaperAdapter
import com.oguzdogdu.wallieshd.util.hide
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.setupRecyclerView
import com.oguzdogdu.wallieshd.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchPhotoFragment :
    BaseFragment<FragmentSearchPhotoBinding>(FragmentSearchPhotoBinding::inflate) {

    private val viewModel: SearchViewModel by activityViewModels()

    private val searchWallpaperAdapter by lazy { SearchWallpaperAdapter() }

    override fun firstExecution(savedInstanceState: Bundle?) {
        super.firstExecution(savedInstanceState)
        viewModel.handleUIEvent(SearchEvent.GetAppLanguageValue)
    }

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        with(binding) {
            recyclerViewSearchPhoto.setupRecyclerView(
                layout = GridLayoutManager(
                    requireContext(),
                    2
                ),
                adapter = searchWallpaperAdapter,
                onScroll = {}
            )
        }
    }

    override fun observeData() {
        super.observeData()
        getSearchPhotoData()
        handlePagingState(searchWallpaperAdapter)
    }

    private fun getSearchPhotoData() {
        viewModel.getSearchContents.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when (state) {
                is SearchState.PhotoState -> {
                    state.searchPhoto.let { searchWallpaperAdapter.submitData(it) }
                }

                else -> {
                }
            }
        })
    }

    private fun handlePagingState(adapter: SearchWallpaperAdapter) {
        searchWallpaperAdapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    binding.recyclerViewSearchPhoto.hide()
                }

                is LoadState.NotLoading -> {
                    binding.recyclerViewSearchPhoto.show()
                }

                else -> {}
            }
            when {
                loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1 -> {
                    binding.recyclerViewSearchPhoto.hide()
                    binding.linearLayoutNoPicture.show()
                }

                else -> {
                    binding.recyclerViewSearchPhoto.show()
                    binding.linearLayoutNoPicture.hide()
                }
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
