package com.oguzdogdu.wallies.presentation.collectionslistsbyid

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentSingleCollectionBinding
import com.oguzdogdu.wallies.util.hide
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.show
import com.oguzdogdu.wallies.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SingleCollectionFragment :
    BaseFragment<FragmentSingleCollectionBinding>(FragmentSingleCollectionBinding::inflate) {

    private val viewModel: CollectionsListsViewModel by viewModels()

    private val collectionsListsAdapter by lazy { CollectionsListsAdapter() }

    private val args: SingleCollectionFragmentArgs by navArgs()

    override fun initViews() {
        super.initViews()
        binding.apply {
            val layoutManager =
                StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
            recyclerViewCollectionsList.layoutManager = layoutManager
            recyclerViewCollectionsList.adapter = collectionsListsAdapter
            recyclerViewCollectionsList.setHasFixedSize(true)
        }
    }

    override fun observeData() {
        super.observeData()
        args.id?.let { viewModel.getCollectionsLists(it) }
        lifecycleScope.launch {
            viewModel.photo.onEach { result ->
                if (result.collectionsLists.isEmpty()) {
                    binding.recyclerViewCollectionsList.hide()
                    binding.linearLayoutNoPicture.show()
                } else {
                    binding.linearLayoutNoPicture.hide()
                    binding.recyclerViewCollectionsList.show()
                    collectionsListsAdapter.submitList(result.collectionsLists)
                }

            }.observeInLifecycle(this@SingleCollectionFragment)
        }
    }
}