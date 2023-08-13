package com.oguzdogdu.wallies.presentation.downloadphoto

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.navArgs
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseBottomSheetDialogFragment
import com.oguzdogdu.wallies.databinding.FragmentDownloadPhotoBinding
import com.oguzdogdu.wallies.util.downloadImage
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DownloadPhotoFragment :
    BaseBottomSheetDialogFragment<FragmentDownloadPhotoBinding>(
        FragmentDownloadPhotoBinding::inflate
    ) {

    private val viewModel: DownloadPhotoViewModel by viewModels()

    private val args: DownloadPhotoFragmentArgs by navArgs()

    override fun initListeners() {
        super.initListeners()
        binding.apply {
            buttonRawQuality.setOnClickListener {
                viewModel.handleUIEvent(DownloadPhotoEvent.ClickedRaw)
            }
            buttonFullQuality.setOnClickListener {
                viewModel.handleUIEvent(DownloadPhotoEvent.ClickedFull)
            }
            buttonMediumQuality.setOnClickListener {
                viewModel.handleUIEvent(DownloadPhotoEvent.ClickedMedium)
            }
            buttonLowQuality.setOnClickListener {
                viewModel.handleUIEvent(DownloadPhotoEvent.ClickedLow)
            }
        }
    }

    override fun observeData() {
        super.observeData()
        getQualityState()
    }

    private fun getQualityState() {
        viewModel.downloadPhotoState.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when (state?.quality) {
                RAW -> downloadImageFromWeb(args.raw)
                FULL -> downloadImageFromWeb(args.high)
                MEDIUM -> downloadImageFromWeb(args.medium)
                LOW -> downloadImageFromWeb(args.low)
            }
        })
    }

    private fun downloadImageFromWeb(url: String?) {
        val directory: String = requireContext().getString(R.string.app_name)
        val fileName = args.imageTitle + FILE_NAME_SUFFIX
        requireView().showToast(requireContext(), R.string.downloading_text, Toast.LENGTH_LONG)
        val downloadableImage = url?.let { requireContext().downloadImage(it, directory, fileName) }
        if (downloadableImage == true) {
            requireView().showToast(
                requireContext(),
                getString(R.string.download_photo_success),
                Toast.LENGTH_LONG
            )
            this.dismiss()
        }
    }
    companion object {
        const val FILE_NAME_SUFFIX = ".jpg"
        const val RAW = "raw"
        const val FULL = "high"
        const val MEDIUM = "medium"
        const val LOW = "low"
    }
}
