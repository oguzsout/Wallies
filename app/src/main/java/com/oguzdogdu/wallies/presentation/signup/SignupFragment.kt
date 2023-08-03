package com.oguzdogdu.wallies.presentation.signup

import android.content.pm.PackageManager
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentSignupBinding
import com.oguzdogdu.wallies.util.FieldValidators
import com.oguzdogdu.wallies.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupFragment : BaseFragment<FragmentSignupBinding>(
    FragmentSignupBinding::inflate
) {
    private val viewModel: SignUpViewModel by viewModels()

    private val REQUEST_CODE_PERMISSIONS = 1001
    private val REQUIRED_PERMISSIONS = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.CAMERA
    )

    @Inject
    lateinit var firebaseFirestore: FirebaseFirestore

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun initViews() {
        super.initViews()
        with(binding) {
            toolbarSignUp.setTitle(
                title = getString(R.string.sign_up_title),
                titleStyleRes = R.style.DialogTitleText
            )
            toolbarSignUp.setLeftIcon(R.drawable.back)
            editTextEmail.addTextChangedListener(TextFieldValidation(binding.editTextEmail))
            editTextPassword.addTextChangedListener(
                TextFieldValidation(binding.editTextPassword)
            )
        }

    }

    private fun checkPermissions() {
        val missingPermissions = REQUIRED_PERMISSIONS.filter {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_DENIED
        }

        if (missingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                missingPermissions.toTypedArray(),
                REQUEST_CODE_PERMISSIONS
            )
        } else {
            pickImageFromGallery()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                pickImageFromGallery()
            }
        }
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                viewModel.setUri(uri)
                binding.imageViewAddUserPhoto.load(uri) {
                    diskCachePolicy(CachePolicy.DISABLED)
                    transformations(CircleCropTransformation())
                    allowConversionToBitmap(true)
                }
            }
        }

    private fun pickImageFromGallery() {
        getContent.launch("image/*")
    }

    override fun initListeners() {
        super.initListeners()
        binding.toolbarSignUp.setLeftIconClickListener {
            navigateWithDirection(SignupFragmentDirections.toLogin())
        }
        binding.imageViewEditPhoto.setOnClickListener {
            checkPermissions()
        }

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
                        navigateBack()
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
                false
            }

            !FieldValidators.isValidEmail(email) -> {
                emailLayout.error = "Invalid Email!"
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
                false
            }

            password.length < 6 -> {
                passwordLayout.error = "Password can't be less than 6"
                false
            }

            !FieldValidators.isStringContainNumber(password) -> {
                passwordLayout.error = "Required at least 1 digit"
                false
            }

            !FieldValidators.isStringLowerAndUpperCase(password) -> {
                passwordLayout.error = "Password must contain upper and lower case letters"
                false
            }

            !FieldValidators.isStringContainSpecialCharacter(password) -> {
                passwordLayout.error = "One special character required"
                false
            }

            else -> {
                passwordLayout.isErrorEnabled = false
                true
            }
        }
    }
}
