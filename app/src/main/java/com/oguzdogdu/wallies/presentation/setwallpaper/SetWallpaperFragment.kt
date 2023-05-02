package com.oguzdogdu.wallies.presentation.setwallpaper

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.Coil
import coil.executeBlocking
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseBottomSheetDialogFragment
import com.oguzdogdu.wallies.databinding.FragmentSetWallpaperBinding
import com.oguzdogdu.wallies.util.showToast
import com.oguzdogdu.wallies.util.toBitmap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL


@AndroidEntryPoint
class SetWallpaperFragment :
    BaseBottomSheetDialogFragment<FragmentSetWallpaperBinding>(FragmentSetWallpaperBinding::inflate) {

    private val args: SetWallpaperFragmentArgs by navArgs()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initListeners() {
        super.initListeners()
            setWallpaperOptions(args.imageUrl)
        }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun setWallpaperOptions(url: String?) {
        binding.apply {
            buttonLockScreen.setOnClickListener {
                requireView().showToast(requireContext(), R.string.set_wallpaper_applied_time)
                setWallpaperFromUrl(url, false)
                requireView().showToast(requireContext(), R.string.set_wallpaper_applied)
            }
            buttonHomeScreen.setOnClickListener {
                requireView().showToast(requireContext(), R.string.set_wallpaper_applied_time)
                setWallpaperFromUrl(url, true)
                requireView().showToast(requireContext(), R.string.set_wallpaper_applied)
            }
            buttonHomeAndLockScreen.setOnClickListener {
                requireView().showToast(requireContext(), R.string.set_wallpaper_applied_time)
                setWallpaperFromUrl(url, null)
                requireView().showToast(requireContext(), R.string.set_wallpaper_applied)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun setWallpaperFromUrl(imageUrl: String?, isLockScreen: Boolean?) {
        lifecycleScope.launch(Dispatchers.IO) {
            val bitmap = imageUrl?.toBitmap()
            val wallpaperManager = WallpaperManager.getInstance(requireContext())
            /*    TODO This field adjusts an image with a high size again according to the size of the phone.
            val displayMetrics = DisplayMetrics()
                val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                windowManager.defaultDisplay.getMetrics(displayMetrics)
                val width = displayMetrics.widthPixels
                val height = displayMetrics.heightPixels
                val bitmap = Bitmap.createScaledBitmap(resource!!, width, height, true)*/
            try {

                when (isLockScreen) {
                    false -> wallpaperManager.setBitmap(
                        bitmap,
                        null,
                        true,
                        WallpaperManager.FLAG_LOCK
                    )

                    null -> wallpaperManager.setBitmap(bitmap)
                    true -> wallpaperManager.setBitmap(
                        bitmap,
                        null,
                        true,
                        WallpaperManager.FLAG_SYSTEM
                    )

                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}