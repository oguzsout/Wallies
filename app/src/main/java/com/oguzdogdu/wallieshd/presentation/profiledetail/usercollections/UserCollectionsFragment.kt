package com.oguzdogdu.wallieshd.presentation.profiledetail.usercollections

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.core.snackbar.MessageType
import com.oguzdogdu.wallieshd.databinding.FragmentUserCollectionsBinding
import com.oguzdogdu.wallieshd.presentation.profiledetail.ProfileDetailEvent
import com.oguzdogdu.wallieshd.presentation.profiledetail.ProfileDetailViewModel
import com.oguzdogdu.wallieshd.util.hide
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.setupRecyclerView
import com.oguzdogdu.wallieshd.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserCollectionsFragment : BaseFragment<FragmentUserCollectionsBinding>(
    FragmentUserCollectionsBinding::inflate
) {
    private val viewModel: ProfileDetailViewModel by activityViewModels()

    private val userCollectionAdapter by lazy { UserCollectionAdapter() }

    override fun initViews() {
        super.initViews()
        binding.rvUserCollections.setupRecyclerView(
            layout = GridLayoutManager(requireContext(), 2),
            adapter = userCollectionAdapter,
            true,
            onScroll = {}
        )
    }

    override fun observeData() {
        super.observeData()
        viewModel.handleUIEvent(
            ProfileDetailEvent.FetchUserCollectionsList
        )
        getUserCollectionList()
    }

    private fun getUserCollectionList() {
        viewModel.getUserCollectionList.observeInLifecycle(
            viewLifecycleOwner,
            ::fetchCollectionListFromState
        )
    }

    private fun fetchCollectionListFromState(state: UserCollectionState?) {
        when (state) {
            is UserCollectionState.Loading -> binding.progressBar.show()
            is UserCollectionState.UserCollectionError -> showMessage(
                message = state.errorMessage.orEmpty(),
                type = MessageType.ERROR
            )

            is UserCollectionState.UserCollections -> {
                binding.progressBar.hide()
                userCollectionAdapter.submitList(state.usersCollection)
            }

            else -> {}
        }
    }
}
