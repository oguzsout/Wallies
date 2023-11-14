package com.oguzdogdu.wallieshd.presentation.setwallpaper

import android.app.WallpaperManager
import android.os.Build
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.Coil
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.oguzdogdu.wallieshd.core.BaseBottomSheetDialogFragment
import com.oguzdogdu.wallieshd.databinding.FragmentSetWallpaperBinding
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers

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
                viewModel.handleUIEvent(
                    SetWallpaperEvent.AdjustWallpaper(place = PlaceOfWallpaper.LOCK_SCREEN.name)
                )
            }
            buttonHomeScreen.setOnClickListener {
                viewModel.handleUIEvent(
                    SetWallpaperEvent.AdjustWallpaper(place = PlaceOfWallpaper.HOME_SCREEN.name)
                )
            }
            buttonHomeAndLockScreen.setOnClickListener {
                viewModel.handleUIEvent(
                    SetWallpaperEvent.AdjustWallpaper(place = PlaceOfWallpaper.HOME_AND_LOCK.name)
                )
            }
        }
    }

    override fun observeData() {
        super.observeData()
        checkWallpaperPlaceState()
    }

    private fun checkWallpaperPlaceState() {
        viewModel.wallpaperState.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when (state) {
                is SetWallpaperState.SetWallpaper -> {
                    setWallpaperFromUrl(imageUrl = args.imageUrl, place = state.finallyPlace)
                }

                is SetWallpaperState.SuccessAdjustImage -> {
                    when (state.isCompleted) {
                        true -> this.dismiss()
                        false -> state.message?.let {
                            requireView().showToast(
                                requireContext(),
                                it,
                                Toast.LENGTH_LONG
                            )
                        }
                        null -> {}
                    }
                }

                else -> {}
            }
        })
    }

    private fun setWallpaperFromUrl(imageUrl: String?, place: String?) {
        val imageLoader = Coil.imageLoader(requireContext())
        val request = ImageRequest.Builder(requireContext())
            .data(imageUrl)
            .transformationDispatcher(Dispatchers.Main.immediate)
            .lifecycle(viewLifecycleOwner)
            .allowConversionToBitmap(true)
            .memoryCachePolicy(CachePolicy.READ_ONLY)
            .target(
                onSuccess = { result ->
                    val wallpaperManager = WallpaperManager.getInstance(requireContext())
                    try {
                        when (place.orEmpty()) {
                            PlaceOfWallpaper.LOCK_SCREEN.name -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                wallpaperManager
                                    .setBitmap(
                                        result.toBitmapOrNull(),
                                        null,
                                        true,
                                        WallpaperManager.FLAG_LOCK
                                    )
                            }

                            PlaceOfWallpaper.HOME_AND_LOCK.name ->
                                wallpaperManager.setBitmap(result.toBitmapOrNull())

                            PlaceOfWallpaper.HOME_SCREEN.name -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                wallpaperManager.setBitmap(
                                    result.toBitmapOrNull(),
                                    null,
                                    true,
                                    WallpaperManager.FLAG_SYSTEM
                                )
                            }
                        }
                        viewModel.handleUIEvent(
                            SetWallpaperEvent.StatusOfAdjustWallpaper(isCompleted = true)
                        )
                    } catch (e: Exception) {
                        viewModel.handleUIEvent(
                            SetWallpaperEvent.StatusOfAdjustWallpaper(
                                isCompleted = false,
                                message = e.message
                            )
                        )
                    }
                },
                onError = { error ->
                    viewModel.handleUIEvent(
                        SetWallpaperEvent.StatusOfAdjustWallpaper(
                            isCompleted = false,
                            message = "Something Went Wrong"
                        )
                    )
                }
            )
            .build()
        imageLoader.enqueue(request)
    }
}
