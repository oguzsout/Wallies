package com.oguzdogdu.wallies.presentation.downloadphoto

import androidx.navigation.fragment.navArgs
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseBottomSheetDialogFragment
import com.oguzdogdu.wallies.databinding.FragmentDownloadPhotoBinding
import com.oguzdogdu.wallies.util.downloadImage

class DownloadPhotoFragment :
    BaseBottomSheetDialogFragment<FragmentDownloadPhotoBinding>(
        FragmentDownloadPhotoBinding::inflate
    ) {

    private val args: DownloadPhotoFragmentArgs by navArgs()

    override fun initListeners() {
        super.initListeners()
        binding.apply {
            buttonRawQuality.setOnClickListener {
                downloadImageFromWeb(args.raw)
            }
            buttonFullQuality.setOnClickListener {
                downloadImageFromWeb(args.high)
            }
            buttonMediumQuality.setOnClickListener {
                downloadImageFromWeb(args.medium)
            }
            buttonLowQuality.setOnClickListener {
                downloadImageFromWeb(args.low)
            }
        }
    }

    private fun downloadImageFromWeb(url: String?) {
        val directory: String = requireContext().getString(R.string.app_name)
        val fileName = "${args.imageTitle}.jpg"
        if (url != null) {
            requireContext().downloadImage(url, directory, fileName)
        }
    }
}
