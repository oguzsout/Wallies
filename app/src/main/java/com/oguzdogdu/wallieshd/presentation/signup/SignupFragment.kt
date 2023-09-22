package com.oguzdogdu.wallieshd.presentation.signup

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.core.snackbar.MessageType
import com.oguzdogdu.wallieshd.databinding.FragmentSignupBinding
import com.oguzdogdu.wallieshd.util.FieldValidators
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SignupFragment : BaseFragment<FragmentSignupBinding>(FragmentSignupBinding::inflate) {

    private val viewModel: SignUpViewModel by viewModels()
    private var photoUri: Uri? = null
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
            uri?.let {
                photoUri = it
                binding.imageViewAddUserPhoto.load(it) {
                    diskCachePolicy(CachePolicy.DISABLED)
                    transformations(CircleCropTransformation())
                    allowConversionToBitmap(true)
                }
            }
        }

    private val singlePhotoPickerLauncher =
        registerForActivityResult(PickVisualMedia()) { uri ->
            uri?.let {
                photoUri = it
                binding.imageViewAddUserPhoto.load(it) {
                    diskCachePolicy(CachePolicy.DISABLED)
                    transformations(CircleCropTransformation())
                    allowConversionToBitmap(true)
                }
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
            viewModel.handleUIEvent(
                SignUpScreenEvent.UserInfosForSignUp(
                    name = binding.editTextName.text.toString(),
                    surname = binding.editTextSurname.text.toString(),
                    email = binding.editTextEmail.text.toString(),
                    password = binding.editTextPassword.text.toString(),
                    photoUri = photoUri ?: Uri.EMPTY
                )
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
        viewModel.handleUIEvent(SignUpScreenEvent.ButtonState)
        checkSignUpState()
    }

    private fun checkSignUpState() {
        viewModel.signUpState.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when (state) {
                is SignUpState.Loading -> {
                }

                is SignUpState.ErrorSignUp -> showMessage(
                    message = state.errorMessage,
                    MessageType.ERROR
                )

                is SignUpState.UserSignUp -> {
                    showMessage(message = getString(R.string.success_sign), MessageType.SUCCESS)
                    delay(3000)
                    navigateWithDirection(
                        SignupFragmentDirections.toMain()
                    )
                }

                is SignUpState.ButtonEnabled -> {
                    binding.buttonSignUp.isEnabled = state.isEnabled
                }

                else -> {}
            }
        })
    }

    private fun setUiComponents() {
        with(binding) {
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
                R.id.editTextEmail -> {
                    viewModel.setEmail(s.toString())
                    viewModel.handleUIEvent(SignUpScreenEvent.ButtonState)
                    FieldValidators.isValidEmailCheck(
                        binding.editTextEmail.text.toString(),
                        binding.emailContainer
                    )
                }

                R.id.editTextPassword -> {
                    viewModel.setPassword(s.toString())
                    viewModel.handleUIEvent(SignUpScreenEvent.ButtonState)
                    FieldValidators.isValidPasswordCheck(
                        binding.editTextPassword.text.toString(),
                        binding.passwordContainer
                    )
                }
            }
        }
    }
}
