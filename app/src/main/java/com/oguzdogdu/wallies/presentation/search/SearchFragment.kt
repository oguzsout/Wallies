package com.oguzdogdu.wallies.presentation.search

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.map
import androidx.recyclerview.widget.GridLayoutManager
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentSearchBinding
import com.oguzdogdu.wallies.presentation.detail.DetailViewModel
import com.oguzdogdu.wallies.presentation.latest.LatestWallpaperAdapter
import com.oguzdogdu.wallies.util.DialogHelper
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.textChanges
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private val viewModel: SearchPhotoViewModel by viewModels()

    private val searchWallpaperAdapter by lazy { SearchWallpaperAdapter() }

    override fun initViews() {
        super.initViews()
        binding.apply {
            recyclerViewSearch.adapter = searchWallpaperAdapter
            val layoutManager = GridLayoutManager(requireContext(), 2)
            recyclerViewSearch.layoutManager = layoutManager
            recyclerViewSearch.setHasFixedSize(true)
        }
    }

    override fun observeData() {
        super.observeData()
        lifecycleScope.launch {
            viewModel.getSearchPhotos.onEach { result ->
                searchWallpaperAdapter.submitData(result.search)
            }.observeInLifecycle(this@SearchFragment)
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun initListeners() {
        super.initListeners()
        binding.apply {
            editTextSearchWalpaper.setOnFocusChangeListener { _, hasFocus ->
                tvCancel.isVisible = hasFocus
            }
            editTextSearchWalpaper.textChanges()
                .onEach {
                    if (it.isNullOrBlank()) {

                    } else {
                        viewModel.getSearchPhotos(it.toString())
                    }
                }
                .launchIn(lifecycleScope)

            editTextSearchWalpaper.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.getSearchPhotos(editTextSearchWalpaper.text.toString())
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
    }

    private fun openKeyboardInSearch() {
        val im: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        im.showSoftInput(activity?.currentFocus, InputMethodManager.SHOW_IMPLICIT)
    }
}