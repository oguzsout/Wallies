package com.oguzdogdu.wallies.presentation.authenticateduser.editprofilephoto

import androidx.fragment.app.viewModels
import coil.load
import com.oguzdogdu.wallies.core.BaseBottomSheetDialogFragment
import com.oguzdogdu.wallies.databinding.DialogEditProfilePhotoBinding
import com.oguzdogdu.wallies.presentation.authenticateduser.AuthenticatedUserScreenState
import com.oguzdogdu.wallies.presentation.authenticateduser.AuthenticedUserFragmentDirections
import com.oguzdogdu.wallies.presentation.authenticateduser.AuthenticedUserViewModel
import com.oguzdogdu.wallies.util.observeInLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfilePhotoDialog : BaseBottomSheetDialogFragment<DialogEditProfilePhotoBinding>(
    DialogEditProfilePhotoBinding::inflate
) {

    private val viewModel: EditProfilePhotoViewModel by viewModels()

    override fun initViews() {
        super.initViews()
    }

    override fun initListeners() {
        super.initListeners()
    }

    override fun observeData() {
        super.observeData()
        viewModel.userImageState.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when (state) {
                is EditProfilePhotoScreenState.ProfileImage -> {
                    binding.imageViewEditProfilePhoto.load(state.image)
                }

                is EditProfilePhotoScreenState.UserInfoError -> {}

                is EditProfilePhotoScreenState.Loading -> {}

                else -> {}
            }
        })
    }
}
