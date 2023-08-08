package com.oguzdogdu.wallies.presentation.authenticateduser.editpassword

import android.widget.Toast
import androidx.fragment.app.viewModels
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentEditPasswordBinding
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditPasswordFragment : BaseFragment<FragmentEditPasswordBinding>(
    FragmentEditPasswordBinding::inflate
) {

    private val viewModel: EditPasswordViewModel by viewModels()

    override fun initViews() {
        super.initViews()
        binding.toolbar.setTitle(
            title = getString(R.string.forgot_password_title),
            titleStyleRes = R.style.ToolbarTitleText
        )
        binding.toolbar.setLeftIcon(R.drawable.back)
        binding.editTextPassword.hint = getString(R.string.password)
    }

    override fun initListeners() {
        super.initListeners()
        binding.toolbar.setLeftIconClickListener {
            navigateWithDirection(EditPasswordFragmentDirections.toAuthUser())
        }
        binding.buttonChangeUserEmail.setOnClickListener {
            viewModel.handleUIEvent(
                EditPasswordScreenEvent.UserPassword(binding.editTextPassword.text.toString())
            )
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.passwordState.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when (state) {
                is EditPasswordScreenState.Loading -> {}
                is EditPasswordScreenState.PasswordChangeError -> {
                    state.errorMessage?.let {
                        requireView().showToast(
                            context = requireContext(),
                            message = it,
                            duration = Toast.LENGTH_LONG
                        )
                    }
                }
                is EditPasswordScreenState.PasswordChangeSucceed -> {
                    if (state.successMessage != null) {
                        requireView().showToast(
                            context = requireContext(),
                            message = state.successMessage,
                            duration = Toast.LENGTH_LONG
                        )
                    }
                }
                else -> {}
            }
        })
    }
}
