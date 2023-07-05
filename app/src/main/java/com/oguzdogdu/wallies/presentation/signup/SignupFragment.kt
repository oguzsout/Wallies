package com.oguzdogdu.wallies.presentation.signup

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseBottomSheetDialogFragment
import com.oguzdogdu.wallies.databinding.FragmentSignupBinding
import com.oguzdogdu.wallies.util.FieldValidators
import com.oguzdogdu.wallies.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupFragment : BaseBottomSheetDialogFragment<FragmentSignupBinding>(
    FragmentSignupBinding::inflate
) {
    private val viewModel: SignUpViewModel by viewModels()
    override fun initViews() {
        super.initViews()
        binding.editTextEmail.addTextChangedListener(TextFieldValidation(binding.editTextEmail))
        binding.editTextPassword.addTextChangedListener(
            TextFieldValidation(binding.editTextPassword)
        )
    }

    override fun initListeners() {
        super.initListeners()
        binding.buttonSignUp.setOnClickListener {
            viewModel.userSignUp(
                name = binding.editTextName.text.toString(),
                surname = binding.editTextSurname.text.toString(),
                email = binding.editTextEmail.text.toString(),
                password = binding.editTextPassword.text.toString()
            )
        }
    }

    override fun observeData() {
        super.observeData()
        lifecycleScope.launch {
            viewModel.signUpState.collect { state ->
                when (state) {
                    is SignUpState.Loading -> {
                    }
                    is SignUpState.ErrorSignUp -> {
                        requireView().showToast(
                            context = requireContext(),
                            message = state.errorMessage,
                            duration = Toast.LENGTH_LONG
                        )
                    }
                    is SignUpState.UserSignUp -> {
                        requireView().showToast(
                            context = requireContext(),
                            message = "Kayıt Başarılı",
                            duration = Toast.LENGTH_LONG
                        )
                        this@SignupFragment.dismiss()
                    }

                    else -> {}
                }
            }
        }
    }
    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (view.id) {
                R.id.editTextEmail -> validateEmail()

                R.id.editTextPassword -> validatePassword()
            }
        }
    }

    private fun validateEmail(): Boolean {
        val email = binding.editTextEmail.text.toString().trim()
        val emailLayout = binding.emailContainer

        return when {
            email.isEmpty() -> {
                emailLayout.error = "Required Field!"
                emailLayout.requestFocus()
                false
            }
            !FieldValidators.isValidEmail(email) -> {
                emailLayout.error = "Invalid Email!"
                emailLayout.requestFocus()
                false
            }
            else -> {
                emailLayout.isErrorEnabled = false
                true
            }
        }
    }

    private fun validatePassword(): Boolean {
        val password = binding.editTextPassword.text.toString().trim()
        val passwordLayout = binding.passwordContainer

        return when {
            password.isEmpty() -> {
                passwordLayout.error = "Required Field!"
                binding.editTextPassword.requestFocus()
                false
            }
            password.length < 6 -> {
                passwordLayout.error = "Password can't be less than 6"
                binding.editTextPassword.requestFocus()
                false
            }
            !FieldValidators.isStringContainNumber(password) -> {
                passwordLayout.error = "Required at least 1 digit"
                binding.editTextPassword.requestFocus()
                false
            }
            !FieldValidators.isStringLowerAndUpperCase(password) -> {
                passwordLayout.error = "Password must contain upper and lower case letters"
                binding.editTextPassword.requestFocus()
                false
            }
            !FieldValidators.isStringContainSpecialCharacter(password) -> {
                passwordLayout.error = "One special character required"
                binding.editTextPassword.requestFocus()
                false
            }
            else -> {
                passwordLayout.isErrorEnabled = false
                true
            }
        }
    }
}
