package com.oguzdogdu.wallies.presentation.authenticateduser.editnameandsurname

import androidx.fragment.app.viewModels
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.R.style.DialogTitleText
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentEditUserNameSurnameBinding
import com.oguzdogdu.wallies.util.filterOnlyLetters
import com.oguzdogdu.wallies.util.observeInLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditUserNameSurnameFragment : BaseFragment<FragmentEditUserNameSurnameBinding>(
    FragmentEditUserNameSurnameBinding::inflate
) {

    private val viewModel: EditUsernameSurnameViewModel by viewModels()

    override fun initViews() {
        super.initViews()
        with(binding) {
            toolbar.setTitle(
                title = getString(R.string.edit_user_info_title),
                titleStyleRes = DialogTitleText
            )
            toolbar.setLeftIcon(R.drawable.back)
            toolbar.setRightIcon(R.drawable.info)
        }
    }

    override fun initListeners() {
        super.initListeners()
        binding.buttonChangeUsernameSurname.setOnClickListener {
            changeUsernameSurname()
            navigateWithDirection(EditUserNameSurnameFragmentDirections.toAuthUser())
        }
        binding.toolbar.setLeftIconClickListener {
            navigateWithDirection(EditUserNameSurnameFragmentDirections.toAuthUser())
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.userState.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when (state) {
                is EditUsernameSurnameScreenState.Loading -> {}
                is EditUsernameSurnameScreenState.UserInfoError -> {}
                is EditUsernameSurnameScreenState.UserInfos -> setUserComponents(
                    name = state.name,
                    surname = state?.surname
                )
                else -> {}
            }
        })
    }

    private fun setUserComponents(name: String?, surname: String?) {
        binding.editTextUserName.hint = getString(R.string.name_text, name)
        binding.editTextSurName.hint = getString(R.string.surname_text, surname)
    }

    private fun changeUsernameSurname() {
        val newUserName = binding.editTextUserName.filterOnlyLetters()
        val newSurName = binding.editTextSurName.filterOnlyLetters()

        if (newUserName.isNotEmpty()) {
            viewModel.handleUIEvent(EditUsernameSurnameEvent.ChangedUserName(name = newUserName))
        }
        if (newSurName.isNotEmpty()) {
            viewModel.handleUIEvent(EditUsernameSurnameEvent.ChangedSurName(surname = newSurName))
        }
    }
}
