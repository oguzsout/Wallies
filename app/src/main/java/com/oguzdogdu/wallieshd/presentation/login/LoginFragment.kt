package com.oguzdogdu.wallieshd.presentation.login

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.text.bold
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputLayout
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.core.snackbar.MessageType
import com.oguzdogdu.wallieshd.databinding.FragmentLoginBinding
import com.oguzdogdu.wallieshd.util.FieldValidators
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var googleSignInClient: GoogleSignInClient

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                viewModel.handleUIEvent(
                    LoginScreenEvent.GoogleButton(idToken = account.idToken)
                )
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    override fun firstExecution(savedInstanceState: Bundle?) {
        super.firstExecution(savedInstanceState)
        val googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
                getString(R.string.default_web_client_id)
            ).requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)
    }

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        setUiComponents()
        setPasswordCleanIcon()
    }

    override fun initListeners() {
        super.initListeners()
        sendLoginRequest()
        binding.loginFieldsContainer.setOnClickListener {
            hideKeyboard()
        }
        binding.textViewSignUp.setOnClickListener {
            navigateWithDirection(LoginFragmentDirections.toSignUp())
        }
        binding.textViewForgetPassword.setOnClickListener {
            navigateWithDirection(LoginFragmentDirections.toForgotPassword())
        }
        binding.buttonGoogleSignIn.setOnClickListener {
            checkVersion()
        }
    }

    private fun checkVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        } else {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                viewModel.handleUIEvent(
                    LoginScreenEvent.GoogleButton(idToken = account.idToken)
                )
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
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
        viewModel.handleUIEvent(LoginScreenEvent.ButtonState)
        checkLoginState()
    }

    private fun checkLoginState() {
        lifecycleScope.launch {
            viewModel.loginState.observeInLifecycle(viewLifecycleOwner, observer = { state ->
                when (state) {
                    is LoginState.ErrorSignIn -> showMessage(
                        message = state.errorMessage,
                        MessageType.ERROR
                    )

                    is LoginState.UserSignIn -> {
                        showMessage(
                            message = getString(R.string.success_login),
                            MessageType.SUCCESS
                        )
                        delay(3000)
                        navigateWithDirection(
                            LoginFragmentDirections.toMain()
                        )
                    }
                    is LoginState.ButtonEnabled -> {
                        binding.button.isEnabled = state.isEnabled
                    }

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

    private fun hideKeyboard() {
        val inputMethodManager =
            requireView().context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (view.id) {
                R.id.emailEt -> {
                    viewModel.setEmail(s.toString())
                    viewModel.handleUIEvent(LoginScreenEvent.ButtonState)
                    FieldValidators.isValidEmailCheck(
                        binding.emailEt.text.toString(),
                        binding.emailLayout
                    )
                }

                R.id.passET -> {
                    viewModel.setPassword(s.toString())
                    viewModel.handleUIEvent(LoginScreenEvent.ButtonState)
                    FieldValidators.isValidPasswordCheck(
                        binding.passET.text.toString(),
                        binding.passwordLayout
                    )
                }
            }
        }
    }
    companion object {
        const val GOOGLE_SIGN_IN = 1001
        const val GOOGLE_CLIENT_ID = "225181955346-fe24mqoaiu3lon9gt5ud0rds0dubrpjp.apps.googleusercontent.com"
    }
}
