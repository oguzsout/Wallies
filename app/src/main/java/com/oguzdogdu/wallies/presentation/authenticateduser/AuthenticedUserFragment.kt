package com.oguzdogdu.wallies.presentation.authenticateduser

import android.text.SpannableStringBuilder
import android.widget.Toast
import androidx.core.text.bold
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentAuthenticedUserBinding
import com.oguzdogdu.wallies.util.Toolbar
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.setupRecyclerView
import com.oguzdogdu.wallies.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticedUserFragment : BaseFragment<FragmentAuthenticedUserBinding>(
    FragmentAuthenticedUserBinding::inflate
) {

    private val profileOptionsList = listOf(
        ProfileMenu(
            iconRes = R.drawable.ic_person, titleRes = R.string.edit_user_info_title
        ), ProfileMenu(
            iconRes = R.drawable.ic_email, titleRes = R.string.edit_email_title
        )
    )

    private val userOptionsAdapter by lazy { ProfileOptionsAdapter() }

    private val viewModel: AuthenticedUserViewModel by viewModels()

    override fun initViews() {
        super.initViews()
        binding.rvUserOptions.setupRecyclerView(layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        ), adapter = userOptionsAdapter, hasFixedSize = true, onScroll = {})
        setDataIntoRV()
    }

    private fun setDataIntoRV() {
        userOptionsAdapter.submitList(profileOptionsList)
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
        userOptionsAdapter.setOnItemClickListener { option ->
            when (option?.titleRes) {
                R.string.edit_user_info_title -> {
                    requireView().showToast(
                        requireContext(), R.string.edit_user_info_title, Toast.LENGTH_LONG
                    )
                }

                R.string.edit_email_title -> {
                    requireView().showToast(
                        requireContext(), R.string.edit_email_title, Toast.LENGTH_LONG
                    )
                }
            }
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
                placeholder(R.drawable.ic_default_avatar)
                allowConversionToBitmap(true)
            }
            val editedString = SpannableStringBuilder().append(getString(R.string.welcome_profile))
                .bold { run { append(", $name ") } }
            textViewWelcome.text = editedString
            textViewUserName.text = getString(R.string.name_text, name)
            textViewSurname.text = getString(R.string.surname_text, surname)
        }
    }
}
