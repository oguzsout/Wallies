package com.oguzdogdu.wallies.presentation.authenticateduser.editemail

import android.widget.Toast
import androidx.fragment.app.viewModels
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentEditEmailBinding
import com.oguzdogdu.wallies.util.ITooltipUtils
import com.oguzdogdu.wallies.util.TooltipDirection
import com.oguzdogdu.wallies.util.information
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditEmailFragment : BaseFragment<FragmentEditEmailBinding>(FragmentEditEmailBinding::inflate) {

    private val viewModel: EditEmailViewModel by viewModels()

    @Inject
    lateinit var tooltip: ITooltipUtils

    override fun initViews() {
        super.initViews()
        with(binding) {
            toolbar.setTitle(
                title = getString(R.string.edit_email),
                titleStyleRes = R.style.DialogTitleText
            )
            toolbar.setLeftIcon(R.drawable.back)
            toolbar.setRightIcon(R.drawable.info)
        }
    }

    override fun initListeners() {
        super.initListeners()
        binding.toolbar.setLeftIconClickListener {
            navigateWithDirection(EditEmailFragmentDirections.toAuthUser())
        }
        binding.toolbar.setRightIconClickListener {
            tooltip.information(
                getString(R.string.change_email_info),
                it,
                viewLifecycleOwner,
                TooltipDirection.TOP
            )
        }
        binding.buttonChangeUserEmail.setOnClickListener {
            changeUserEmail()
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.emailState.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when (state) {
                is EditEmailScreenState.Loading -> {}
                is EditEmailScreenState.UserInfoError -> {
                    state.errorMessage?.let {
                        requireView().showToast(
                            requireContext(),
                            it,
                            Toast.LENGTH_LONG
                        )
                    }
                }
                is EditEmailScreenState.UserEmail -> setDataToUiComponent(email = state.email)
                else -> {}
            }
        })
    }

    private fun setDataToUiComponent(email: String?) {
        binding.editTextEmail.hint = "Email: $email"
    }

    private fun changeUserEmail() {
        val newEmail = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()

        if (newEmail.isNotEmpty()) {
            viewModel.handleUIEvent(
                EditUserEmailEvent.ChangedEmail(email = newEmail, password = password)
            )
        }
    }
}
