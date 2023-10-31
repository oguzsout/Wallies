package com.oguzdogdu.wallieshd.presentation.authenticateduser.editprofilephoto

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseBottomSheetDialogFragment
import com.oguzdogdu.wallieshd.databinding.DialogEditProfilePhotoBinding
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfilePhotoDialog :
    BaseBottomSheetDialogFragment<DialogEditProfilePhotoBinding>(
        DialogEditProfilePhotoBinding::inflate
    ) {

    private val viewModel: EditProfilePhotoViewModel by viewModels()

    private lateinit var photoUri: Uri

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
                photoUri = uri
                setUserComponent(it)
            }
        }

    private val singlePhotoPickerLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                photoUri = uri
                binding.imageViewEditProfilePhoto.load(it)
                setUserComponent(it)
            }
        }

    override fun initViews() {
        super.initViews()
        when (args.profilePhoto.isNullOrBlank()) {
            true -> binding.imageViewEditProfilePhoto.load(R.drawable.ic_default_avatar)
            false -> binding.imageViewEditProfilePhoto.load(args.profilePhoto) {
                diskCachePolicy(CachePolicy.DISABLED)
                transformations(CircleCropTransformation())
                allowConversionToBitmap(true)
            }
        }
    }

    override fun initListeners() {
        super.initListeners()
        binding.imageViewEditProfilePhoto.setOnClickListener {
            checkPermissions()
        }
        binding.buttonChangeUserPhoto.setOnClickListener {
            viewModel.handleUiEvents(EditProfilePhotoEvent.ChangeProfileImage(photoUri = photoUri))
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
                    when (state.isCompleted) {
                        true -> navigateWithDirection(EditProfilePhotoDialogDirections.toAuthUser())
                        false -> requireView().showToast(
                            requireContext(),
                            "Something went wrong",
                            Toast.LENGTH_LONG
                        )

                        else -> {}
                    }
                }

                else -> {}
            }
        })
    }

    private fun setUserComponent(image: Uri?) {
        when (image.toString().isBlank()) {
            true -> binding.imageViewEditProfilePhoto.load(R.drawable.ic_default_avatar)
            false -> binding.imageViewEditProfilePhoto.load(image) {
                diskCachePolicy(CachePolicy.DISABLED)
                transformations(CircleCropTransformation())
                allowConversionToBitmap(true)
            }
        }
    }
}
