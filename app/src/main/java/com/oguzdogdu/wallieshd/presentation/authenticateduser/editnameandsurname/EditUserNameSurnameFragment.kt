package com.oguzdogdu.wallieshd.presentation.authenticateduser.editnameandsurname

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.databinding.FragmentEditUserNameSurnameBinding
import com.oguzdogdu.wallieshd.util.filterOnlyLetters
import com.oguzdogdu.wallieshd.util.observeInLifecycle
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
                titleStyleRes = R.style.DialogTitleText
            )
            toolbar.setLeftIcon(R.drawable.back)
        }
        binding.editTextUserName.addTextChangedListener(
            TextFieldValidation(binding.editTextUserName)
        )
        binding.editTextSurName.addTextChangedListener(TextFieldValidation(binding.editTextSurName))
        binding.buttonChangeUsernameSurname.isEnabled =
            !(binding.editTextUserName.text.isNullOrEmpty() && binding.editTextSurName.text.isNullOrEmpty())
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
                is EditUsernameSurnameScreenState.UserInfos -> {}
                else -> {
                }
            }
        })
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

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            binding.buttonChangeUsernameSurname.isEnabled = !s.isNullOrBlank()
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            binding.buttonChangeUsernameSurname.isEnabled = !s.isNullOrBlank()
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.buttonChangeUsernameSurname.isEnabled = !s.isNullOrBlank()
        }
    }
}
