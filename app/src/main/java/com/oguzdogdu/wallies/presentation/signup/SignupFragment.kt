package com.oguzdogdu.wallies.presentation.signup

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentSignupBinding
import com.oguzdogdu.wallies.util.FieldValidators
import com.oguzdogdu.wallies.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupFragment : BaseFragment<FragmentSignupBinding>(FragmentSignupBinding::inflate) {

    private val viewModel: SignUpViewModel by viewModels()

    private val REQUEST_CODE_PERMISSIONS = 1001
    private val PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES
        )
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
    }

    private val pickProfilePictureFromGalleryResult =
        registerForActivityResult(OpenDocument()) { uri: Uri? ->
            viewModel.setUri(uri)
            binding.imageViewAddUserPhoto.load(uri) {
                diskCachePolicy(CachePolicy.DISABLED)
                transformations(CircleCropTransformation())
                allowConversionToBitmap(true)
            }
        }

    private val singlePhotoPickerLauncher =
        registerForActivityResult(PickVisualMedia()) { uri ->
            viewModel.setUri(uri)
            binding.imageViewAddUserPhoto.load(uri) {
                diskCachePolicy(CachePolicy.DISABLED)
                transformations(CircleCropTransformation())
                allowConversionToBitmap(true)
            }
        }

    override fun initViews() {
        super.initViews()
        setUiComponents()
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

    private fun checkPermissions() {
        val missingPermissions = PERMISSIONS.filter {
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

    private fun pickImageFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            singlePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ImageOnly)
            )
        } else {
            pickProfilePictureFromGalleryResult.launch(arrayOf("image/*"))
        }
    }

    override fun observeData() {
        super.observeData()
        checkSignUpState()
    }

    private fun checkSignUpState(){
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

                    is SignUpState.UserSignUp -> navigateWithDirection(
                        SignupFragmentDirections.toMain()
                    )

                    else -> {}
                }
            }
        }
    }

    private fun setUiComponents() {
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

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (view.id) {
                R.id.editTextEmail -> FieldValidators.isValidEmailCheck(
                    binding.editTextEmail.text.toString(),
                    binding.emailContainer
                )

                R.id.editTextPassword -> FieldValidators.isValidPasswordCheck(
                    binding.editTextPassword.text.toString(),
                    binding.passwordContainer
                )
            }
        }
    }
}
