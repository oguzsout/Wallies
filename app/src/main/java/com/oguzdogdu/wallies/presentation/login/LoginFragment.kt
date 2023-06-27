package com.oguzdogdu.wallies.presentation.login

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentLoginBinding
import com.oguzdogdu.wallies.util.FieldValidators.isStringContainNumber
import com.oguzdogdu.wallies.util.FieldValidators.isStringContainSpecialCharacter
import com.oguzdogdu.wallies.util.FieldValidators.isStringLowerAndUpperCase
import com.oguzdogdu.wallies.util.FieldValidators.isValidEmail
import com.oguzdogdu.wallies.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel: LoginViewModel by viewModels()
    override fun initViews() {
        super.initViews()
        binding.button.setOnClickListener {
            viewModel.signIn(
                userEmail = binding.emailEt.text.toString(),
                password = binding.passET.text.toString()
            )
        }
    }

    override fun initListeners() {
        super.initListeners()
        binding.emailEt.addTextChangedListener(TextFieldValidation(binding.emailEt))
        binding.passET.addTextChangedListener(TextFieldValidation(binding.passET))
    }

    override fun observeData() {
        super.observeData()
        lifecycleScope.launch {
            viewModel.loginState.collect { state ->
                when (state) {
                    is LoginState.UserNotSignIn -> {
                        requireView().showToast(
                            context = requireContext(),
                            message = "Lütfen giriş yap",
                            duration = Toast.LENGTH_LONG
                        )
                    }

                    is LoginState.Loading -> {
                    }

                    is LoginState.ErrorSignIn -> {
                        requireView().showToast(
                            context = requireContext(),
                            message = state.errorMessage,
                            duration = Toast.LENGTH_LONG
                        )
                    }

                    is LoginState.UserSignIn -> {
                        navigate(R.id.toMain, null)
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
                R.id.emailEt -> {
                    validateEmail()
                }

                R.id.passET -> {
                    validatePassword()
                }
            }
        }
    }

    private fun validateEmail(): Boolean {
        if (binding.emailEt.text.toString().trim().isEmpty()) {
            binding.emailLayout.error = "Required Field!"
            binding.emailEt.requestFocus()
            return false
        } else if (!isValidEmail(binding.emailEt.text.toString())) {
            binding.emailLayout.error = "Invalid Email!"
            binding.emailEt.requestFocus()
            return false
        } else {
            binding.emailLayout.isErrorEnabled = false
        }
        return true
    }

    private fun validatePassword(): Boolean {
        if (binding.passET.text.toString().trim().isEmpty()) {
            binding.passwordLayout.error = "Required Field!"
            binding.passET.requestFocus()
            return false
        } else if (binding.passET.text.toString().length < 6) {
            binding.passwordLayout.error = "Password can't be less than 6"
            binding.passET.requestFocus()
            return false
        } else if (!isStringContainNumber(binding.passET.text.toString())) {
            binding.passwordLayout.error = "Required at least 1 digit"
            binding.passET.requestFocus()
            return false
        } else if (!isStringLowerAndUpperCase(binding.passET.text.toString())) {
            binding.passwordLayout.error =
                "Password must contain upper and lower case letters"
            binding.passET.requestFocus()
            return false
        } else if (!isStringContainSpecialCharacter(binding.passET.text.toString())) {
            binding.passwordLayout.error = "One special character required"
            binding.passET.requestFocus()
            return false
        } else {
            binding.passwordLayout.isErrorEnabled = false
        }
        return true
    }
}