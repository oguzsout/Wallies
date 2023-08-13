package com.oguzdogdu.wallies.presentation.setwallpaper

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.oguzdogdu.wallies.core.BaseBottomSheetDialogFragment
import com.oguzdogdu.wallies.databinding.FragmentSetWallpaperBinding
import com.oguzdogdu.wallies.util.observeInLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetWallpaperFragment :
    BaseBottomSheetDialogFragment<FragmentSetWallpaperBinding>(FragmentSetWallpaperBinding::inflate) {

    private val viewModel: SetWallpaperViewModel by viewModels()

    private val args: SetWallpaperFragmentArgs by navArgs()

    override fun initListeners() {
        super.initListeners()
        setWallpaperOptions()
    }

    private fun setWallpaperOptions() {
        binding.apply {
            buttonLockScreen.setOnClickListener {
                viewModel.handleUIEvent(SetWallpaperEvent.AdjustWallpaper(LOCK_SCREEN))
            }
            buttonHomeScreen.setOnClickListener {
                viewModel.handleUIEvent(SetWallpaperEvent.AdjustWallpaper(HOME_SCREEN))
            }
            buttonHomeAndLockScreen.setOnClickListener {
                viewModel.handleUIEvent(SetWallpaperEvent.AdjustWallpaper(HOME_AND_LOCK))
            }
        }
    }

    override fun observeData() {
        super.observeData()
        checkWallpaperPlaceState()
    }

    private fun checkWallpaperPlaceState() {
        viewModel.wallpaperState.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            setWallpaperFromUrl(imageUrl = args.imageUrl, place = state?.finallyPlace)
        })
    }

    private fun setWallpaperFromUrl(imageUrl: String?, place: String?) {
        Glide.with(requireContext())
            .asBitmap().load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                @RequiresApi(Build.VERSION_CODES.N)
                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    val wallpaperManager = WallpaperManager.getInstance(requireContext())
                    try {
                        resource?.let {
                            when (place) {
                                LOCK_SCREEN -> wallpaperManager.setBitmap(
                                    it,
                                    null,
                                    true,
                                    WallpaperManager.FLAG_LOCK
                                )

                                HOME_AND_LOCK -> wallpaperManager.setBitmap(it)
                                HOME_SCREEN -> wallpaperManager.setBitmap(
                                    it,
                                    null,
                                    true,
                                    WallpaperManager.FLAG_SYSTEM
                                )

                                else -> {}
                            }
                        }
                        this@SetWallpaperFragment.dismiss()
                        return true
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return false
                }
            }).submit()
    }

    companion object {
        const val LOCK_SCREEN = "Lock Screen"
        const val HOME_AND_LOCK = "Home and Lock Screen"
        const val HOME_SCREEN = "Home Screen"
    }
}
