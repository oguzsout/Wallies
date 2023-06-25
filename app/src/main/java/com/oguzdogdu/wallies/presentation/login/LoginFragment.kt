package com.oguzdogdu.wallies.presentation.login

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentLoginBinding
import com.oguzdogdu.wallies.util.FieldValidators.isStringContainNumber
import com.oguzdogdu.wallies.util.FieldValidators.isStringContainSpecialCharacter
import com.oguzdogdu.wallies.util.FieldValidators.isStringLowerAndUpperCase
import com.oguzdogdu.wallies.util.FieldValidators.isValidEmail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    override fun initViews() {
        super.initViews()
        binding.button.setOnClickListener {
            navigate(R.id.toMain, null)
        }
    }

    override fun initListeners() {
        super.initListeners()
        binding.emailEt.addTextChangedListener(TextFieldValidation(binding.emailEt))
        binding.passET.addTextChangedListener(TextFieldValidation(binding.passET))
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