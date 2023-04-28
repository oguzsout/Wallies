package com.oguzdogdu.wallies.presentation.collectionslistsbyid

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentSingleCollectionBinding
import com.oguzdogdu.wallies.util.CheckConnection
import com.oguzdogdu.wallies.util.hide
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.setUp
import com.oguzdogdu.wallies.util.show
import com.oguzdogdu.wallies.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SingleCollectionFragment :
    BaseFragment<FragmentSingleCollectionBinding>(FragmentSingleCollectionBinding::inflate) {

    @Inject
    lateinit var connection : CheckConnection

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

    override fun initListeners() {
        super.initListeners()
        collectionsListsAdapter.setOnItemClickListener {
            val arguments = Bundle().apply {
                putString("id", it?.id)
            }
            navigate(R.id.toDetail,arguments)
        }
    }

    override fun observeData() {
        super.observeData()
        checkConnection()
    }

    private fun checkConnection(){
        connection.observe(this@SingleCollectionFragment) { isConnected ->
            when (isConnected) {
                true -> {
                    collectionDetailList()
                }
                false -> {
                    requireView().showToast(requireContext(), R.string.internet_error)
                }
                null -> {}
            }
        }
    }

    private fun collectionDetailList(){
        args.id?.let { viewModel.getCollectionsLists(it) }
        lifecycleScope.launch {
            viewModel.photo.onEach { result ->
                when {
                    result.isLoading -> {}
                    result.collectionsLists.isEmpty() -> {
                        binding.linearLayoutNoPicture.show()
                    }
                    else -> {
                        binding.linearLayoutNoPicture.hide()
                        collectionsListsAdapter.submitList(result.collectionsLists)
                    }
                }
            }.observeInLifecycle(this@SingleCollectionFragment)
        }
    }
}