package com.oguzdogdu.wallies.presentation.search

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentSearchBinding
import com.oguzdogdu.wallies.util.CheckConnection
import com.oguzdogdu.wallies.util.observe
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.setupRecyclerView
import com.oguzdogdu.wallies.util.show
import com.oguzdogdu.wallies.util.showToast
import com.oguzdogdu.wallies.util.textChanges
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

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
                onScroll = {})
        }
    }

    override fun initListeners() {
        super.initListeners()
        binding.apply {
            buttonBack.setOnClickListener {
                navigateBack()
            }
            searchWallpaperAdapter.setOnItemClickListener {
                val arguments = Bundle().apply {
                    putString("id", it?.id)
                }
                navigate(R.id.toDetail, arguments)
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
                    lifecycleScope.launch {
                        viewModel.eventFlow.collectLatest { event ->
                            when (event) {
                                is SearchEvent.Success -> {
                                  searchWallpaperAdapter.submitData(viewModel.getSearchPhotos.value.search)
                                }
                                else -> {}
                            }

                        }
                    }
                }

                false -> {
                    requireView().showToast(requireContext(), R.string.internet_error)
                }

                null -> {}
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
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
                .observeInLifecycle(this@SearchFragment)

            editTextSearchWalpaper.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.handleUIEvent(SearchEvent.EnteredSearchQuery(editTextSearchWalpaper.text.toString()))
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