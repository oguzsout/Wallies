package com.oguzdogdu.wallieshd.presentation.authenticateduser.editemail

import androidx.fragment.app.viewModels
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.core.snackbar.MessageType
import com.oguzdogdu.wallieshd.databinding.FragmentEditEmailBinding
import com.oguzdogdu.wallieshd.util.ITooltipUtils
import com.oguzdogdu.wallieshd.util.TooltipDirection
import com.oguzdogdu.wallieshd.util.information
import com.oguzdogdu.wallieshd.util.observeInLifecycle
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
                is EditEmailScreenState.UserInfoError -> showMessage(
                    message = state.errorMessage.orEmpty(),
                    type = MessageType.ERROR
                )
                is EditEmailScreenState.UserEmail -> setDataToUiComponent(email = state.email)
                else -> {}
            }
        })
    }

    private fun setDataToUiComponent(email: String?) {
        binding.editTextEmail.hint = "Email: $email"
        binding.editTextPassword.hint = getString(R.string.password)
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
