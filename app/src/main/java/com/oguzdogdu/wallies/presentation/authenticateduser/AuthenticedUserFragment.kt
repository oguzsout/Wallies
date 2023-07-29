package com.oguzdogdu.wallies.presentation.authenticateduser

import androidx.fragment.app.viewModels
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentAuthenticedUserBinding
import com.oguzdogdu.wallies.util.Toolbar
import com.oguzdogdu.wallies.util.observeInLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticedUserFragment : BaseFragment<FragmentAuthenticedUserBinding>(
    FragmentAuthenticedUserBinding::inflate
) {

    private val viewModel: AuthenticedUserViewModel by viewModels()

    override fun initViews() {
        super.initViews()
    }

    override fun initListeners() {
        super.initListeners()
        binding.toolbarProfile.onLeftClickListener = object : Toolbar.OnLeftClickListener {
            override fun onLeftButtonClick() {
                navigateBack()
            }
        }
        binding.buttonSignOut.setOnClickListener {
            viewModel.handleUiEvents(AuthenticatedUserEvent.SignOut)
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.userState.observeInLifecycle(viewLifecycleOwner) { state ->
            when (state) {
                is AuthenticatedUserScreenState.UserInfos -> {
                    setUserComponents(
                        name = state.name,
                        surname = state.surname,
                        profileImage = state.profileImage
                    )
                }

                is AuthenticatedUserScreenState.UserInfoError -> {
                }

                is AuthenticatedUserScreenState.Loading -> {
                }

                is AuthenticatedUserScreenState.CheckUserAuthStatus -> {
                    when (state.isAuthenticated) {
                        false -> navigateWithDirection(AuthenticedUserFragmentDirections.toLogin())
                        true -> {}
                    }
                }

                else -> {}
            }
        }
    }

    private fun setUserComponents(name: String?, surname: String?, profileImage: String?) {
        with(binding) {
            imageViewProfileImage.load(profileImage) {
                diskCachePolicy(CachePolicy.DISABLED)
                transformations(CircleCropTransformation())
                allowConversionToBitmap(true)
            }
            textViewUserName.text = "Name: $name"
            textViewSurname.text = "Surname: $surname"
        }
    }
}
