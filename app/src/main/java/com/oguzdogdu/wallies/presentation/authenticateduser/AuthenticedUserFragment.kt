package com.oguzdogdu.wallies.presentation.authenticateduser

import android.text.SpannableStringBuilder
import android.view.View
import androidx.core.content.ContextCompat
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
import com.oguzdogdu.wallies.util.OptionLists
import com.oguzdogdu.wallies.util.TooltipDirection
import com.oguzdogdu.wallies.util.information
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.setupRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticedUserFragment :
    BaseFragment<FragmentAuthenticedUserBinding>(
        FragmentAuthenticedUserBinding::inflate
    ) {

    @Inject
    lateinit var tooltip: ITooltipUtils

    private val userOptionsAdapter by lazy { ProfileOptionsAdapter() }

    private val viewModel: AuthenticedUserViewModel by viewModels()

    override fun initViews() {
        super.initViews()
        binding.rvUserOptions.setupRecyclerView(
            layout = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            ),
            adapter = userOptionsAdapter,
            hasFixedSize = true,
            addDivider = true,
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
        val optionList = OptionLists.profileOptionsList
        userOptionsAdapter.submitList(optionList)
        userOptionsAdapter.onBindToDivider = { binding, position ->
            setItemBackground(
                items = optionList,
                itemSize = optionList.size,
                adapter = userOptionsAdapter,
                position = position,
                binding = binding
            )
        }
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
                R.string.edit_user_info_title -> navigateWithDirection(
                    AuthenticedUserFragmentDirections.toEditUserName()
                )

                R.string.edit_email_title -> navigateWithDirection(
                    AuthenticedUserFragmentDirections.toEditEmail()
                )

                R.string.forgot_password_title -> navigateWithDirection(
                    AuthenticedUserFragmentDirections.toUpdatePassword()
                )
            }
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.handleUiEvents(AuthenticatedUserEvent.FetchUserInfos)
        viewModel.userState.observeInLifecycle(viewLifecycleOwner) { state ->
            when (state) {
                is AuthenticatedUserScreenState.UserInfos -> {
                    setUserComponents(
                        name = state.name,
                        surname = state.surname,
                        profileImage = state.profileImage
                    )
                    goToChangeProfilePhoto(image = state.profileImage)
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

    private fun goToChangeProfilePhoto(image: String?) {
        binding.imageViewEditPhoto.setOnClickListener {
            navigateWithDirection(AuthenticedUserFragmentDirections.toEditProfilePhoto(image))
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

    private fun setItemBackground(
        itemSize: Int,
        position: Int,
        items: List<ProfileMenu>,
        adapter: ProfileOptionsAdapter,
        binding: View
    ) {
        adapter.currentList.indexOf(items[position])

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
