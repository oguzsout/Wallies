package com.oguzdogdu.wallies.presentation.authenticateduser

import android.text.SpannableStringBuilder
import androidx.core.text.bold
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentAuthenticedUserBinding
import com.oguzdogdu.wallies.util.ITooltipUtils
import com.oguzdogdu.wallies.util.TooltipDirection
import com.oguzdogdu.wallies.util.information
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.setupRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticedUserFragment : BaseFragment<FragmentAuthenticedUserBinding>(
    FragmentAuthenticedUserBinding::inflate
) {

    private val profileOptionsList = listOf(
        ProfileMenu(
            iconRes = R.drawable.ic_person,
            titleRes = R.string.edit_user_info_title
        ),
        ProfileMenu(
            iconRes = R.drawable.ic_email,
            titleRes = R.string.edit_email_title
        )
    )

    @Inject
    lateinit var tooltip: ITooltipUtils

    private val userOptionsAdapter by lazy { ProfileOptionsAdapter() }

    private val viewModel: AuthenticedUserViewModel by viewModels()

    override fun initViews() {
        super.initViews()
        binding.rvUserOptions.setupRecyclerView(
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            ),
            adapter = userOptionsAdapter,
            hasFixedSize = true,
            onScroll = {}
        )
        setDataIntoRV()
        binding.toolbarProfile.setTitle(
            title = getString(R.string.profile_title),
            titleStyleRes = R.style.ToolbarTitleText
        )
        binding.toolbarProfile.setLeftIcon(R.drawable.back)
    }

    private fun setDataIntoRV() {
        userOptionsAdapter.submitList(profileOptionsList)
    }

    override fun initListeners() {
        super.initListeners()
        binding.imageViewShowInfo.setOnClickListener {
            tooltip.information(
                getString(R.string.show_info_edit_infos),
                it,
                viewLifecycleOwner,
                TooltipDirection.TOP
            )
        }
        binding.toolbarProfile.setLeftIconClickListener {
            navigateWithDirection(AuthenticedUserFragmentDirections.toMain())
        }
        binding.buttonSignOut.setOnClickListener {
            viewModel.handleUiEvents(AuthenticatedUserEvent.SignOut)
            navigateWithDirection(AuthenticedUserFragmentDirections.toLogin())
        }
        userOptionsAdapter.setOnItemClickListener { option ->
            when (option?.titleRes) {
                R.string.edit_user_info_title -> {
                    navigateWithDirection(AuthenticedUserFragmentDirections.toEditUserName())
                }

                R.string.edit_email_title -> {
                    navigateWithDirection(AuthenticedUserFragmentDirections.toEditEmail())
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
            if (profileImage?.isNotEmpty() == true) {
                imageViewProfileImage.load(profileImage) {
                    diskCachePolicy(CachePolicy.DISABLED)
                    transformations(CircleCropTransformation())
                    allowConversionToBitmap(true)
                }
            } else {
                imageViewProfileImage.load(R.drawable.ic_default_avatar)
            }

            val editedString = SpannableStringBuilder().append(getString(R.string.welcome_profile))
                .bold { run { append(", $name ") } }
            textViewWelcome.text = editedString
            textViewUserName.text = getString(R.string.name_text, name)
            textViewSurname.text = getString(R.string.surname_text, surname)
        }
    }
}
