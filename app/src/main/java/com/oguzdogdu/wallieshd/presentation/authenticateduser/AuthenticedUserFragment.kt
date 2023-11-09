package com.oguzdogdu.wallieshd.presentation.authenticateduser

import android.text.SpannableStringBuilder
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.databinding.FragmentAuthenticedUserBinding
import com.oguzdogdu.wallieshd.util.ITooltipUtils
import com.oguzdogdu.wallieshd.util.OptionLists
import com.oguzdogdu.wallieshd.util.TooltipDirection
import com.oguzdogdu.wallieshd.util.hide
import com.oguzdogdu.wallieshd.util.information
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.setupRecyclerView
import com.oguzdogdu.wallieshd.util.show
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
        binding.textViewRequireLogin.setOnClickListener {
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
        viewModel.handleUiEvents(AuthenticatedUserEvent.CheckUserAuth)
        setUiData()
    }

    private fun setUiData() {
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

                is AuthenticatedUserScreenState.UserInfoError -> {}

                is AuthenticatedUserScreenState.Loading -> {}

                is AuthenticatedUserScreenState.CheckUserAuthenticated -> {
                    when (state.isAuthenticated) {
                        true -> {
                            viewModel.handleUiEvents(AuthenticatedUserEvent.FetchUserInfos)
                            setUiData()
                            binding.rvUserOptions.show()
                            binding.cardViewUserInfoContainer.show()
                            binding.buttonSignOut.show()
                            binding.userNotAuthCaution.hide()
                        }
                        else -> {
                            setIfUserNotAuthGoToAuth()
                            binding.userNotAuthCaution.show()
                            binding.rvUserOptions.hide()
                            binding.cardViewUserInfoContainer.hide()
                            binding.buttonSignOut.hide()
                        }
                    }
                }

                is AuthenticatedUserScreenState.CheckUserGoogleSignIn -> {
                    when (state.isAuthenticated) {
                        true -> {
                            binding.rvUserOptions.hide()
                            binding.imageViewEditPhoto.hide()
                        }

                        false -> {
                            binding.rvUserOptions.show()
                            binding.imageViewEditPhoto.show()
                        }
                    }
                }

                else -> {}
            }
        }
    }

    private fun setIfUserNotAuthGoToAuth() {
        val editedString = SpannableStringBuilder()
            .append(getString(R.string.user_not_auth_caution))
            .bold { run { append(" ${getString(R.string.click_for_login)}   ") } }
        binding.textViewRequireLogin.text = editedString
    }

    private fun goToChangeProfilePhoto(image: String?) {
        binding.imageViewEditPhoto.setOnClickListener {
            navigateWithDirection(AuthenticedUserFragmentDirections.toEditProfilePhoto(image))
        }
    }

    private fun setUserComponents(name: String?, surname: String?, profileImage: String?) {
        with(binding) {
            when (profileImage.isNullOrBlank()) {
                false -> imageViewProfileImage.load(profileImage) {
                    diskCachePolicy(CachePolicy.DISABLED)
                    transformations(CircleCropTransformation())
                    allowConversionToBitmap(true)
                }

                true -> imageViewProfileImage.load(R.drawable.ic_default_avatar)
            }
            val editedString = SpannableStringBuilder().append(getString(R.string.welcome_profile))
                .bold { run { append(", ${name.orEmpty()} \uD83D\uDD90️ ") } }
            textViewWelcome.text = editedString
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
