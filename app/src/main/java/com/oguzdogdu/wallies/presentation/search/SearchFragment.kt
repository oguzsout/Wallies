package com.oguzdogdu.wallies.presentation.search

import android.content.Context
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.core.snackbar.MessageType
import com.oguzdogdu.wallies.databinding.FragmentSearchBinding
import com.oguzdogdu.wallies.util.LoaderAdapter
import com.oguzdogdu.wallies.util.hide
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.setupRecyclerView
import com.oguzdogdu.wallies.util.show
import com.oguzdogdu.wallies.util.textChanges
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private val viewModel: SearchPhotoViewModel by viewModels()

    private val searchWallpaperAdapter by lazy { SearchWallpaperAdapter() }

    override fun initViews() {
        super.initViews()
        binding.apply {
            recyclerViewSearch.setupRecyclerView(
                layout = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL),
                adapter = searchWallpaperAdapter.withLoadStateFooter(LoaderAdapter()),
                true,
                onScroll = {}
            )
        }
    }

    override fun initListeners() {
        super.initListeners()
        binding.apply {
            buttonBack.setOnClickListener {
                navigateBack()
            }
            searchWallpaperAdapter.setOnItemClickListener {
                navigateWithDirection(SearchFragmentDirections.toDetail(id = it?.id))
            }
        }
        searchToImages()
    }

    override fun observeData() {
        super.observeData()
        showSearchDatas()
        handlePagingState()
    }

    private fun showSearchDatas() {
        viewModel.getSearchPhotos.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            state?.let { searchWallpaperAdapter.submitData(it.search) }
        })
    }

    private fun searchToImages() {
        binding.apply {
            editTextSearchWalpaper.setOnFocusChangeListener { _, hasFocus ->
                tvCancel.isVisible = hasFocus
            }

            editTextSearchWalpaper.textChanges()
                .onEach {
                    if (it?.isNotEmpty() == true) {
                        viewModel.handleUIEvent(SearchEvent.EnteredSearchQuery(it.toString()))
                    }
                }
                .observeInLifecycle(lifecycleOwner = viewLifecycleOwner, observer = {})

            editTextSearchWalpaper.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.handleUIEvent(
                        SearchEvent.EnteredSearchQuery(editTextSearchWalpaper.text.toString())
                    )
                }
                true
            }

            editTextSearchWalpaper.requestFocus()

            openKeyboardInSearch()

            tvCancel.setOnClickListener {
                tvCancel.isVisible = false
                editTextSearchWalpaper.text?.clear()
                editTextSearchWalpaper.clearFocus()
            }
        }
    }

    private fun openKeyboardInSearch() {
        val im: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        im.showSoftInput(activity?.currentFocus, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun handlePagingState() {
        searchWallpaperAdapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    binding.progressBar.show()
                    binding.recyclerViewSearch.hide()
                }
                is LoadState.NotLoading -> {
                    binding.progressBar.hide()
                    binding.recyclerViewSearch.show()
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
