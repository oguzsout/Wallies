package com.oguzdogdu.wallies.presentation.unsplashprofile

import androidx.navigation.fragment.navArgs
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.oguzdogdu.wallies.core.BaseBottomSheetDialogFragment
import com.oguzdogdu.wallies.databinding.FragmentProfileDescriptionBinding
import com.oguzdogdu.wallies.util.hide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileDescriptionFragment : BaseBottomSheetDialogFragment<FragmentProfileDescriptionBinding>(
    FragmentProfileDescriptionBinding::inflate
) {

    private val args: ProfileDescriptionFragmentArgs by navArgs()

    override fun initViews() {
        super.initViews()
        binding.apply {
            imageViewProfilePhoto.load(args.imageUrl ?: "") {
                diskCachePolicy(CachePolicy.DISABLED)
                transformations(CircleCropTransformation())
                allowConversionToBitmap(true)
                textViewProfileName.text = args.name
                textViewLocation.text = args.location
                when {
                    args.location.isNullOrBlank() -> textViewLocation.hide()
                    args.imageUrl.isNullOrBlank() -> imageViewProfilePhoto.hide()
                    args.name.isNullOrBlank() -> textViewProfileName.hide()
                }
            }
        }
    }

    override fun initListeners() {
        super.initListeners()
        navigateWebProfile()
    }

    private fun navigateWebProfile() {
        binding.textViewMore.setOnClickListener {
            navigateWithDirection(
                ProfileDescriptionFragmentDirections.toProfileDetail(args.username)
            )
        }
    }
}
