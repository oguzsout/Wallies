package com.oguzdogdu.wallies.presentation.login.forgotmypassword

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.viewModels
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentForgotMyPasswordBinding
import com.oguzdogdu.wallies.util.FieldValidators
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotMyPasswordFragment : BaseFragment<FragmentForgotMyPasswordBinding>(
    FragmentForgotMyPasswordBinding::inflate
) {

    private val viewModel: ForgotMyPasswordViewModel by viewModels()

    override fun initViews() {
        super.initViews()
        with(binding) {
            editTextSetEmail.addTextChangedListener(TextFieldValidation(binding.editTextSetEmail))
            toolbarForgotPassword.setTitle(
                title = getString(R.string.forgot_password_title),
                titleStyleRes = R.style.DialogTitleText
            )
            toolbarForgotPassword.setLeftIcon(R.drawable.back)
        }
    }

    override fun initListeners() {
        super.initListeners()
        binding.button.setOnClickListener {
            viewModel.handleUIEvent(
                ForgotPasswordScreenEvent.SendEmail(
                    email = binding.editTextSetEmail.text.toString()
                )
            )
        }
        binding.toolbarForgotPassword.setLeftIconClickListener {
            navigateWithDirection(ForgotMyPasswordFragmentDirections.toLogin())
        }
    }

    override fun observeData() {
        super.observeData()
    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (view.id) {
                R.id.editTextSetEmail -> FieldValidators.isValidEmailCheck(
                    binding.editTextSetEmail.text.toString(),
                    binding.emailLayout
                )
            }
        }
    }
}