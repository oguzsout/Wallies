package com.oguzdogdu.wallies.presentation.collectionslistsbyid

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentSingleCollectionBinding
import com.oguzdogdu.wallies.util.CheckConnection
import com.oguzdogdu.wallies.util.hide
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.show
import com.oguzdogdu.wallies.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SingleCollectionFragment :
    BaseFragment<FragmentSingleCollectionBinding>(FragmentSingleCollectionBinding::inflate) {

    @Inject
    lateinit var connection: CheckConnection

    private val viewModel: CollectionsListsViewModel by viewModels()

    private val collectionsListsAdapter by lazy { CollectionsListsAdapter() }

    private val args: SingleCollectionFragmentArgs by navArgs()

    override fun initViews() {
        super.initViews()
        binding.apply {
            val layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
            recyclerViewCollectionsList.layoutManager = layoutManager
            recyclerViewCollectionsList.adapter = collectionsListsAdapter
            recyclerViewCollectionsList.setHasFixedSize(true)
            toolbar.title = args.title ?: ""
        }
    }

    override fun initListeners() {
        super.initListeners()
        collectionsListsAdapter.setOnItemClickListener {
            navigateWithDirection(SingleCollectionFragmentDirections.toDetail(id = it?.id))
        }

        binding.toolbar.setNavigationOnClickListener {
            navigateBack()
        }
    }

    override fun observeData() {
        super.observeData()
        checkConnection()
    }

    private fun checkConnection() {
        connection.observe(this@SingleCollectionFragment) { isConnected ->
            when (isConnected) {
                true -> {
                    getListByCategory()
                }

                false -> {
                    requireView().showToast(requireContext(), R.string.internet_error)
                }

                null -> {}
            }
        }
    }

    private fun getListByCategory() {
        args.id?.let { viewModel.getCollectionsLists(it) }
        viewModel.photo.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when {
                state.isLoading -> {}
                state.error.isNotEmpty() -> {}
                state.collectionsLists.isEmpty() -> binding.linearLayoutNoPicture.show()

                else -> {
                    binding.linearLayoutNoPicture.hide()
                    collectionsListsAdapter.submitList(state.collectionsLists)
                }
            }
        })
    }
}
