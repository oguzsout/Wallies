package com.oguzdogdu.wallieshd.presentation.authenticateduser.editpassword

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.core.snackbar.MessageType
import com.oguzdogdu.wallieshd.databinding.FragmentEditPasswordBinding
import com.oguzdogdu.wallieshd.util.FieldValidators
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
        with(binding) {
            toolbar.setTitle(
                title = getString(R.string.forgot_password_title),
                titleStyleRes = R.style.DialogTitleText
            )
            toolbar.setLeftIcon(R.drawable.back)
            editTextPassword.addTextChangedListener(
                TextFieldValidation(binding.editTextPassword)
            )
        }
    }

    override fun initListeners() {
        super.initListeners()
        binding.toolbar.setLeftIconClickListener {
            navigateWithDirection(EditPasswordFragmentDirections.toAuthUser())
        }
        binding.buttonChangeUserPassword.setOnClickListener {
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

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (view.id) {
                R.id.editTextPassword -> {
                    FieldValidators.isValidPasswordCheck(
                        binding.editTextPassword.text.toString(),
                        binding.editTextPasswordLayout
                    )
                }
            }
        }
    }
}
