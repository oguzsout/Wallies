package com.oguzdogdu.wallies.presentation.unsplashprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.databinding.FragmentProfileDescriptionBinding
import com.oguzdogdu.wallies.util.hide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileDescriptionFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentProfileDescriptionBinding

    private val args: ProfileDescriptionFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            imageViewProfilePhoto.load(args.imageUrl ?: "") {
                diskCachePolicy(CachePolicy.DISABLED)
                transformations(CircleCropTransformation())
                allowConversionToBitmap(true)
                textViewProfileName.text = args.name
                textViewBioDesc.text = args.bio
                textViewLocation.text = args.location
                when {
                    args.location.isNullOrBlank() -> textViewLocation.hide()
                    args.imageUrl.isNullOrBlank() -> imageViewProfilePhoto.hide()
                    args.bio.isNullOrBlank() -> textViewBioDesc.hide()
                    args.name.isNullOrBlank() -> textViewProfileName.hide()
                }
            }
        }
        navigateWebProfile()
    }
    private fun navigateWebProfile(){
        binding.textViewMore.setOnClickListener {
            val arguments = Bundle().apply {
                putString("url", args.profileUrl)
            }
            findNavController().navigate(R.id.toWebProfile,arguments)
        }
    }
}