package com.oguzdogdu.wallieshd.presentation.profiledetail.userphotos

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.core.snackbar.MessageType
import com.oguzdogdu.wallieshd.databinding.FragmentUserPhotosBinding
import com.oguzdogdu.wallieshd.presentation.profiledetail.ProfileDetailEvent
import com.oguzdogdu.wallieshd.presentation.profiledetail.ProfileDetailFragmentDirections
import com.oguzdogdu.wallieshd.presentation.profiledetail.ProfileDetailViewModel
import com.oguzdogdu.wallieshd.util.hide
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.setupRecyclerView
import com.oguzdogdu.wallieshd.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserPhotosFragment : BaseFragment<FragmentUserPhotosBinding>(
    FragmentUserPhotosBinding::inflate
) {

    private val viewModel: ProfileDetailViewModel by activityViewModels()

    private val usersPhotosAdapter by lazy { UsersPhotosAdapter() }

    override fun initViews() {
        super.initViews()
        binding.rvUserPhotos.setupRecyclerView(
            layout = GridLayoutManager(requireContext(), 2),
            adapter = usersPhotosAdapter,
            true,
            onScroll = {}
        )
    }

    override fun initListeners() {
        super.initListeners()
        usersPhotosAdapter.setOnItemClickListener {
            navigateWithDirection(ProfileDetailFragmentDirections.toDetail(it?.id))
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.handleUIEvent(ProfileDetailEvent.FetchUserPhotosList)
        getUserPhotoList()
    }

    private fun getUserPhotoList() {
        viewModel.getUserPhotoList.observeInLifecycle(viewLifecycleOwner, ::fetchPhotoListFromState)
    }

    private fun fetchPhotoListFromState(state: UserPhotosState?) {
        when (state) {
            is UserPhotosState.Loading -> binding.progressBar.show()
            is UserPhotosState.UserPhotosError -> showMessage(
                message = state.errorMessage.orEmpty(),
                type = MessageType.ERROR
            )

            is UserPhotosState.UserPhotos -> {
                binding.progressBar.hide()
                usersPhotosAdapter.submitList(state.usersPhotos)
            }

            else -> {}
        }
    }
}
