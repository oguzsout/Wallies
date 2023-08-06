package com.oguzdogdu.wallies.presentation.authenticateduser.editprofilephoto

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.oguzdogdu.wallies.core.BaseBottomSheetDialogFragment
import com.oguzdogdu.wallies.databinding.DialogEditProfilePhotoBinding
import com.oguzdogdu.wallies.util.observeInLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfilePhotoDialog :
    BaseBottomSheetDialogFragment<DialogEditProfilePhotoBinding>(
        DialogEditProfilePhotoBinding::inflate
    ) {

    private val viewModel: EditProfilePhotoViewModel by viewModels()

    private val args: EditProfilePhotoDialogArgs by navArgs()

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
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                setUserComponent(it)
                viewModel.setUri(it)
            }
        }

    private val singlePhotoPickerLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                binding.imageViewEditProfilePhoto.load(it) {
                    setUserComponent(it)
                    viewModel.setUri(it)
                }
            }
        }

    override fun initViews() {
        super.initViews()
        args.profilePhoto?.let {
            setUserComponent(it.toUri())
        }
    }

    override fun initListeners() {
        super.initListeners()
        binding.imageViewEditProfilePhoto.setOnClickListener {
            checkPermissions()
        }
        binding.buttonChangeUserPhoto.setOnClickListener {
            viewModel.handleUiEvents(EditProfilePhotoEvent.ChangeProfileImage)
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
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        } else {
            pickProfilePictureFromGalleryResult.launch(arrayOf("image/*"))
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.userImageState.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when (state) {
                is EditProfilePhotoScreenState.Loading -> {}
                is EditProfilePhotoScreenState.UserInfoError -> {}
                is EditProfilePhotoScreenState.ProcessCompleted -> {
                    if (state.isCompleted == true) {
                        navigateWithDirection(EditProfilePhotoDialogDirections.toAuthUser())
                    }
                }

                else -> {}
            }
        })
    }

    private fun setUserComponent(image: Uri?) {
        binding.imageViewEditProfilePhoto.load(image) {
            diskCachePolicy(CachePolicy.DISABLED)
            transformations(CircleCropTransformation())
            allowConversionToBitmap(true)
        }
    }
}
