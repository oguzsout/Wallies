package com.oguzdogdu.wallies.presentation.collections

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.map
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentCollectionsBinding
import com.oguzdogdu.wallies.presentation.search.SearchPhotoViewModel
import com.oguzdogdu.wallies.presentation.search.SearchWallpaperAdapter
import com.oguzdogdu.wallies.util.hide
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CollectionsFragment :
    BaseFragment<FragmentCollectionsBinding>(FragmentCollectionsBinding::inflate) {

    private val viewModel: CollectionViewModel by viewModels()

    private val collectionListAdapter by lazy { CollectionListAdapter() }

    override fun initViews() {
        super.initViews()
        binding.apply {
            val layoutManager =
                GridLayoutManager(requireContext(), 2)
            recyclerViewCollections.layoutManager = layoutManager
            recyclerViewCollections.adapter = collectionListAdapter
            recyclerViewCollections.setHasFixedSize(true)
        }
    }

    override fun initListeners() {
        super.initListeners()
        collectionListAdapter.setOnItemClickListener {
            val arguments = Bundle().apply {
                putString("id", it?.id)
            }
            navigate(R.id.toCollectionsLists, arguments)
        }
    }

    override fun observeData() {
        super.observeData()
        lifecycleScope.launch {
            viewModel.getCollections.onEach { result ->
                when {
                    result.isLoading -> {

                    }
                    result.error.isNotEmpty() -> {

                    }
                    else -> {
                        collectionListAdapter.submitData(result.collections)
                    }
                }
            }.observeInLifecycle(this@CollectionsFragment)
        }
    }
}