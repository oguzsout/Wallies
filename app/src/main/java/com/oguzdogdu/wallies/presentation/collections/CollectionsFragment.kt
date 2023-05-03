package com.oguzdogdu.wallies.presentation.collections

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentCollectionsBinding
import com.oguzdogdu.wallies.presentation.main.MainActivity
import com.oguzdogdu.wallies.util.CheckConnection
import com.oguzdogdu.wallies.util.observe
import com.oguzdogdu.wallies.util.setupRecyclerView
import com.oguzdogdu.wallies.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CollectionsFragment :
    BaseFragment<FragmentCollectionsBinding>(FragmentCollectionsBinding::inflate) {

    @Inject
    lateinit var connection: CheckConnection

    private val viewModel: CollectionViewModel by viewModels()

    private val collectionListAdapter by lazy { CollectionListAdapter() }

    override fun initViews() {
        super.initViews()
        binding.apply {
            recyclerViewCollections.setupRecyclerView(
                layoutManager = GridLayoutManager(requireContext(), 2),
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
            val arguments = Bundle().apply {
                putString("id", it?.id)
                putString("title", it?.desc)
            }
            navigate(R.id.toCollectionsLists, arguments)
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.getCollectionsList()
        checkConnection()
    }

    private fun checkConnection() {
        connection.observe(this@CollectionsFragment) { isConnected ->
            when (isConnected) {
                true -> {
                    observe(viewModel.getCollections, viewLifecycleOwner) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            when {
                                it.isLoading -> {}

                                it.error.isNotEmpty() -> {}

                                else -> {
                                    collectionListAdapter.submitData(it.collections)
                                }
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
}