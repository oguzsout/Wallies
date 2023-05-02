package com.oguzdogdu.wallies.presentation.downloadphoto

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseBottomSheetDialogFragment
import com.oguzdogdu.wallies.databinding.FragmentDownloadPhotoBinding
import com.oguzdogdu.wallies.presentation.setwallpaper.SetWallpaperFragmentArgs
import com.oguzdogdu.wallies.util.downloadImage
import com.oguzdogdu.wallies.util.showToast
import java.io.File

class DownloadPhotoFragment : BaseBottomSheetDialogFragment<FragmentDownloadPhotoBinding>(FragmentDownloadPhotoBinding::inflate) {

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
        val directory : String = requireContext().getString(R.string.app_name)
        val fileName = "${args.imageTitle}.jpg"
        if (url != null) {
            requireContext().downloadImage(url, directory, fileName)
        }
    }
}