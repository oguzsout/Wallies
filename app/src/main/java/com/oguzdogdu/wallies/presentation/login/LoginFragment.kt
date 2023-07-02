package com.oguzdogdu.wallies.presentation.login

import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.text.bold
import androidx.core.text.scale
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
        val s = SpannableStringBuilder()
            .append("Not Registered Yet")
            .bold { run { append(", Sign Up !  ") } }
        binding.textViewSignUp.text = s
    }

    override fun initListeners() {
        super.initListeners()
        sendLoginRequest()
        binding.textViewSignUp.setOnClickListener {
            navigate(R.id.toSignUp,null)
        }
    }

    private fun sendLoginRequest(){
        binding.emailEt.addTextChangedListener(TextFieldValidation(binding.emailEt))
        binding.passET.addTextChangedListener(TextFieldValidation(binding.passET))
        binding.button.setOnClickListener {
            viewModel.handleUIEvent(
                LoginScreenEvent.UserSignIn(
                    email = binding.emailEt.text.toString(),
                    password = binding.passET.text.toString()
                )
            )
        }
    }

    override fun observeData() {
        super.observeData()
        lifecycleScope.launch {
            viewModel.loginState.collect { state ->
                when (state) {
                    is LoginState.UserNotSignIn -> {}

                    is LoginState.Loading -> {}

                    is LoginState.ErrorSignIn -> requireView().showToast(context = requireContext(), message = state.errorMessage, duration = Toast.LENGTH_LONG)

                    is LoginState.UserSignIn ->  navigate(R.id.toMain, null)

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
                R.id.emailEt -> validateEmail()

                R.id.passET -> validatePassword()
            }
        }
    }

    private fun validateEmail(): Boolean {
        val email = binding.emailEt.text.toString().trim()
        val emailLayout = binding.emailLayout

        return when {
            email.isEmpty() -> {
                emailLayout.error = "Required Field!"
                emailLayout.requestFocus()
                false
            }
            !isValidEmail(email) -> {
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
        val password = binding.passET.text.toString().trim()
        val passwordLayout = binding.passwordLayout

        return when {
            password.isEmpty() -> {
                passwordLayout.error = "Required Field!"
                binding.passET.requestFocus()
                false
            }
            password.length < 6 -> {
                passwordLayout.error = "Password can't be less than 6"
                binding.passET.requestFocus()
                false
            }
            !isStringContainNumber(password) -> {
                passwordLayout.error = "Required at least 1 digit"
                binding.passET.requestFocus()
                false
            }
            !isStringLowerAndUpperCase(password) -> {
                passwordLayout.error = "Password must contain upper and lower case letters"
                binding.passET.requestFocus()
                false
            }
            !isStringContainSpecialCharacter(password) -> {
                passwordLayout.error = "One special character required"
                binding.passET.requestFocus()
                false
            }
            else -> {
                passwordLayout.isErrorEnabled = false
                true
            }
        }
    }
}