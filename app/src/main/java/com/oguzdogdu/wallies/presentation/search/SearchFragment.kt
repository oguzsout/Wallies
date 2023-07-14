package com.oguzdogdu.wallies.presentation.search

import android.content.Context
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentSearchBinding
import com.oguzdogdu.wallies.util.CheckConnection
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.setupRecyclerView
import com.oguzdogdu.wallies.util.showToast
import com.oguzdogdu.wallies.util.textChanges
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    @Inject
    lateinit var connection: CheckConnection

    private val viewModel: SearchPhotoViewModel by viewModels()

    private val searchWallpaperAdapter by lazy { SearchWallpaperAdapter() }

    override fun initViews() {
        super.initViews()
        binding.apply {
            recyclerViewSearch.setupRecyclerView(
                layoutManager = GridLayoutManager(requireContext(), 2),
                adapter = searchWallpaperAdapter,
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
        checkConnection()
    }

    private fun checkConnection() {
        connection.observe(viewLifecycleOwner) { isConnected ->
            when (isConnected) {
                true -> {
                    viewModel.eventFlow.observeInLifecycle(viewLifecycleOwner, observer = { event ->
                        when (event) {
                            is SearchEvent.Success -> searchWallpaperAdapter.submitData(
                                viewModel.getSearchPhotos.value.search
                            )

                            else -> {}
                        }
                    })
                }

                false -> requireView().showToast(requireContext(), R.string.internet_error)

                null -> {}
            }
        }
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
}
