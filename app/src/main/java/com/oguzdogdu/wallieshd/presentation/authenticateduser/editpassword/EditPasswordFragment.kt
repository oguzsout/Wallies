package com.oguzdogdu.wallieshd.presentation.authenticateduser.editpassword

import android.widget.Toast
import androidx.fragment.app.viewModels
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.core.snackbar.MessageType
import com.oguzdogdu.wallieshd.databinding.FragmentEditPasswordBinding
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.showToast
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
                is EditPasswordScreenState.PasswordChangeError -> showMessage(
                    message = state.errorMessage.orEmpty(),
                    type = MessageType.ERROR
                )
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
