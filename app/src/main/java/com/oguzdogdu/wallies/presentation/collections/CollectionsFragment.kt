package com.oguzdogdu.wallies.presentation.collections

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentCollectionsBinding
import com.oguzdogdu.wallies.presentation.main.MainActivity
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.setupRecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionsFragment :
    BaseFragment<FragmentCollectionsBinding>(FragmentCollectionsBinding::inflate) {

    private val viewModel: CollectionViewModel by viewModels()

    private val collectionListAdapter by lazy { CollectionListAdapter() }

    override fun initViews() {
        super.initViews()
        binding.apply {
            recyclerViewCollections.setupRecyclerView(
                layoutManager = GridLayoutManager(requireContext(), 3),
                adapter = collectionListAdapter,
                true,
                onScroll = {
                    recyclerViewCollections.addOnScrollListener(object :
                            RecyclerView.OnScrollListener() {
                            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                                super.onScrolled(recyclerView, dx, dy)
                                if (dy > 0) {
                                    (activity as MainActivity).slideDown()
                                } else if (dy < 0) {
                                    (activity as MainActivity).slideUp()
                                }
                            }
                        })
                }
            )
        }
    }

    override fun initListeners() {
        super.initListeners()
        collectionListAdapter.setOnItemClickListener {
            navigateWithDirection(
                CollectionsFragmentDirections.toCollectionsLists(
                    id = it?.id,
                    title = it?.title
                )
            )
        }
    }

    override fun observeData() {
        super.observeData()
        showCollectionsDatas()
    }

    private fun showCollectionsDatas() {
        viewModel.getCollectionsList()
        viewModel.getCollections.observeInLifecycle(
            viewLifecycleOwner,
            observer = { state ->
                when {
                    state.isLoading -> {}

                    state.error.isNotEmpty() -> {}

                    else -> collectionListAdapter.submitData(state.collections)
                }
            }
        )
    }
}
