package com.oguzdogdu.wallieshd.presentation.search.searchuser

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.core.snackbar.MessageType
import com.oguzdogdu.wallieshd.databinding.FragmentSearchUserBinding
import com.oguzdogdu.wallieshd.presentation.search.SearchState
import com.oguzdogdu.wallieshd.presentation.search.SearchViewModel
import com.oguzdogdu.wallieshd.util.hide
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.setupRecyclerView
import com.oguzdogdu.wallieshd.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchUserFragment : BaseFragment<FragmentSearchUserBinding>(
    FragmentSearchUserBinding::inflate
) {

    private val viewModel: SearchViewModel by activityViewModels()

    private val searchUserAdapter by lazy { SearchUserAdapter() }

    override fun firstExecution(savedInstanceState: Bundle?) {
        super.firstExecution(savedInstanceState)
    }

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        with(binding) {
            recyclerViewSearchUser.setupRecyclerView(
                layout = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false),
                adapter = searchUserAdapter,
                addDivider = true,
                onScroll = {}
            )
        }
    }

    override fun observeData() {
        super.observeData()
        getSearchPhotoData()
        handlePagingState(searchUserAdapter)
        searchUserAdapter.onBindToDivider = { binding, position ->
            setItemBackground(
                itemSize = searchUserAdapter.itemCount,
                position = position,
                binding = binding
            )
        }
    }

    private fun getSearchPhotoData() {
        viewModel.getSearchContents.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when (state) {
                is SearchState.UserState -> {
                    state.searchUser.let {
                        searchUserAdapter.submitData(it)
                    }
                }

                else -> {
                }
            }
        })
    }

    private fun handlePagingState(adapter: SearchUserAdapter) {
        searchUserAdapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    binding.recyclerViewSearchUser.hide()
                }

                is LoadState.NotLoading -> {
                    binding.recyclerViewSearchUser.show()
                }

                else -> {}
            }
            when {
                loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1 -> {
                    binding.recyclerViewSearchUser.hide()
                    binding.linearLayoutNoPicture.show()
                }

                else -> {
                    binding.recyclerViewSearchUser.show()
                    binding.linearLayoutNoPicture.hide()
                }
            }

            val errorState = when {
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }
            errorState?.let {
                it.error.message?.let { message ->
                    showMessage(message = message, type = MessageType.ERROR)
                }
            }
        }
    }

    private fun setItemBackground(
        itemSize: Int,
        position: Int,
        binding: View
    ) {
        when (itemSize) {
            1 -> binding.background = ContextCompat.getDrawable(
                binding.context,
                R.drawable.bg_clickable_all_radius_10
            )
            else -> {
                when (position) {
                    0 -> {
                        binding.background = ContextCompat.getDrawable(
                            binding.context,
                            R.drawable.bg_clickable_top_radius_10
                        )
                    }

                    itemSize - 1 -> binding.background = ContextCompat.getDrawable(
                        binding.context,
                        R.drawable.bg_clickable_bottom_radius_10
                    )

                    else -> {
                        binding.background = ContextCompat.getDrawable(
                            binding.context,
                            R.drawable.bg_clickable_no_radius
                        )
                    }
                }
            }
        }
    }
}
