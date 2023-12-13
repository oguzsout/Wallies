package com.oguzdogdu.wallieshd.presentation.search

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.core.snackbar.MessageType
import com.oguzdogdu.wallieshd.databinding.FragmentSearchBinding
import com.oguzdogdu.wallieshd.util.LoaderAdapter
import com.oguzdogdu.wallieshd.util.hide
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.setupRecyclerView
import com.oguzdogdu.wallieshd.util.show
import com.oguzdogdu.wallieshd.util.textChanges
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private val viewModel: SearchPhotoViewModel by viewModels()

    private val searchWallpaperAdapter by lazy { SearchWallpaperAdapter() }

    private val suggestSearchWordsAdapter by lazy { SuggestSearchWordsAdapter() }

    private var appLang: String? = null

    override fun firstExecution(savedInstanceState: Bundle?) {
        super.firstExecution(savedInstanceState)
        appLang = requireContext().resources?.configuration?.locale?.language
        when (requireContext().resources?.configuration?.locale?.language) {
            "en-US" -> appLang = "en"
        }
    }

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        binding.apply {
            recyclerViewSearch.setupRecyclerView(
                layout = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL),
                adapter = searchWallpaperAdapter.withLoadStateFooter(LoaderAdapter()),
                true,
                onScroll = {}
            )
            recyclerViewSuggestSearchWords.setupRecyclerView(
                layout = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false),
                adapter = suggestSearchWordsAdapter,
                hasFixedSize = true,
                onScroll = {}
            )
        }
        val tag = this.arguments?.getString("tag")
        if (tag != null) {
            binding.editTextSearchWalpaper.setText(tag)
            viewModel.handleUIEvent(
                SearchEvent.EnteredSearchQuery(tag, appLang)
            )
        }
    }

    override fun initListeners() {
        super.initListeners()
        binding.buttonBack.setOnClickListener { navigateBack() }
        searchWallpaperAdapter.setOnItemClickListener {
            navigateWithDirection(SearchFragmentDirections.toDetail(id = it?.id))
        }
        suggestSearchWordsAdapter.setOnItemClickListener { suggest ->
            val suggestSearch = suggest?.keyword.orEmpty()
            binding.editTextSearchWalpaper.setText(suggestSearch)
            viewModel.handleUIEvent(
                SearchEvent.EnteredSearchQuery(suggest?.keyword.orEmpty(), appLang)
            )
            binding.tvCancel.show()
        }
        searchToImages()
    }

    override fun observeData() {
        super.observeData()
        showSearchDatas()
        handlePagingState()
        setSuggestionKeywordToAdapter()
    }

    private fun setSuggestionKeywordToAdapter() {
        val list = mutableListOf(
            SuggestWords(getString(R.string.search_animal)),
            SuggestWords(getString(R.string.search_jungle)),
            SuggestWords(getString(R.string.search_natural)),
            SuggestWords(getString(R.string.search_space)),
            SuggestWords(getString(R.string.search_cars)),
            SuggestWords(getString(R.string.search_walley)),
            SuggestWords(getString(R.string.search_cat)),
            SuggestWords(getString(R.string.search_rain))
        )
        suggestSearchWordsAdapter.submitList(list)
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
                        viewModel.handleUIEvent(
                            SearchEvent.EnteredSearchQuery(it.toString(), appLang)
                        )
                    }
                }
                .observeInLifecycle(lifecycleOwner = viewLifecycleOwner, observer = {})

            editTextSearchWalpaper.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.handleUIEvent(
                        SearchEvent.EnteredSearchQuery(
                            editTextSearchWalpaper.text.toString(),
                            appLang
                        )
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
