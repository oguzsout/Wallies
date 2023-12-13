package com.oguzdogdu.wallieshd.presentation.collectionslistsbyid

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.core.snackbar.MessageType
import com.oguzdogdu.wallieshd.databinding.FragmentSingleCollectionBinding
import com.oguzdogdu.wallieshd.util.hide
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingleCollectionFragment :
    BaseFragment<FragmentSingleCollectionBinding>(FragmentSingleCollectionBinding::inflate) {

    private val viewModel: CollectionsListsViewModel by viewModels()

    private val collectionsListsAdapter by lazy { CollectionsListsAdapter() }

    private val args: SingleCollectionFragmentArgs by navArgs()

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        binding.apply {
            val layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
            recyclerViewCollectionsList.layoutManager = layoutManager
            recyclerViewCollectionsList.adapter = collectionsListsAdapter
            recyclerViewCollectionsList.setHasFixedSize(true)
            args.title?.let {
                toolbar.setTitle(
                    title = it,
                    titleStyleRes = R.style.DetailText,
                    titleCentered = true
                )
            }
            toolbar.setLeftIcon(R.drawable.back)
        }
    }

    override fun initListeners() {
        super.initListeners()
        collectionsListsAdapter.setOnItemClickListener {
            navigateWithDirection(SingleCollectionFragmentDirections.toDetail(id = it?.id))
        }

        binding.toolbar.setLeftIconClickListener {
            navigateBack()
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.handleUiEvents(CollectionListEvent.FetchCollectionList(id = args.id))
        getListByCategory()
    }

    private fun getListByCategory() {
        viewModel.photo.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when (state) {
                is CollectionsListsState.Loading -> {
                    binding.progressBar.show()
                }
                is CollectionsListsState.CollectionListError -> showMessage(
                    message = state.errorMessage.orEmpty(),
                    type = MessageType.ERROR
                )

                is CollectionsListsState.CollectionList -> {
                    when {
                        state.collectionsLists?.isEmpty() == true -> {
                            binding.linearLayoutNoPicture.show()
                            binding.progressBar.hide()
                        }
                        state.collectionsLists?.isNotEmpty() == true -> {
                            binding.linearLayoutNoPicture.hide()
                            binding.progressBar.hide()
                            collectionsListsAdapter.submitList(state.collectionsLists)
                        }
                    }
                }
                else -> {}
            }
        })
    }
}
