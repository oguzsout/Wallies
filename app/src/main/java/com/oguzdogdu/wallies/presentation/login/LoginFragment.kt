package com.oguzdogdu.wallies.presentation.login

import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.text.bold
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentLoginBinding
import com.oguzdogdu.wallies.util.FieldValidators
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel: LoginViewModel by viewModels()

    override fun initViews() {
        super.initViews()
        setUiComponents()
        setPasswordCleanIcon()
    }

    override fun initListeners() {
        super.initListeners()
        sendLoginRequest()
        binding.textViewSignUp.setOnClickListener {
            navigateWithDirection(LoginFragmentDirections.toSignUp())
        }
        binding.textViewForgetPassword.setOnClickListener {
            navigateWithDirection(LoginFragmentDirections.toForgotPassword())
        }
    }

    private fun sendLoginRequest() {
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
        checkLoginState()
    }

    private fun checkLoginState() {
        lifecycleScope.launch {
            viewModel.loginState.observeInLifecycle(viewLifecycleOwner, observer = { state ->
                when (state) {
                    is LoginState.ErrorSignIn -> requireView().showToast(
                        context = requireContext(),
                        message = state.errorMessage,
                        duration = Toast.LENGTH_LONG
                    )

                    is LoginState.UserSignIn -> navigateWithDirection(
                        LoginFragmentDirections.toMain()
                    )

                    else -> {}
                }
            })
        }
    }

    private fun setUiComponents() {
        with(binding) {
            val editedString = SpannableStringBuilder()
                .append(getString(R.string.not_registered))
                .bold { run { append(" ${getString(R.string.sign_up_title)} !  ") } }
            textViewSignUp.text = editedString
            emailEt.addTextChangedListener(TextFieldValidation(binding.emailEt))
            passET.addTextChangedListener(TextFieldValidation(binding.passET))
        }
    }

    private fun setPasswordCleanIcon() {
        if (binding.emailEt.text.toString().isNotEmpty()) {
            binding.emailLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
            binding.emailLayout.setEndIconDrawable(R.drawable.ic_clear_text)
            binding.emailLayout.setEndIconOnClickListener {
                binding.emailEt.text?.clear()
            }
        }
    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (view.id) {
                R.id.emailEt -> FieldValidators.isValidEmailCheck(
                    binding.emailEt.text.toString(),
                    binding.emailLayout
                )

                R.id.passET -> FieldValidators.isValidPasswordCheck(
                    binding.passET.text.toString(),
                    binding.passwordLayout
                )
            }
        }
    }
}
