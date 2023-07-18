package com.oguzdogdu.wallies.presentation.detail

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.oguzdogdu.domain.model.singlephoto.Photo
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentDetailBinding
import com.oguzdogdu.wallies.util.CheckConnection
import com.oguzdogdu.wallies.util.formatDate
import com.oguzdogdu.wallies.util.itemLoading
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.showToast
import com.oguzdogdu.wallies.util.toFormattedString
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    @Inject
    lateinit var connection: CheckConnection

    private val viewModel: DetailViewModel by viewModels()

    private val args: DetailFragmentArgs by navArgs()

    override fun initListeners() {
        super.initListeners()

        binding.toolbar.setNavigationOnClickListener {
            navigateBack()
        }
    }

    override fun observeData() {
        super.observeData()
        checkConnection()
    }

    private fun checkConnection() {
        connection.observe(this@DetailFragment) { isConnected ->
            when (isConnected) {
                true -> {
                    args.id?.let { viewModel.getSinglePhoto(it) }
                    viewModel.photo.observeInLifecycle(viewLifecycleOwner, observer = { state ->
                        setItems(state.detail)
                        favoriteCheck(state.detail?.id)
                        showProfileInfos(state.detail)
                        navigateToSetWallpaper(state.detail?.urls)
                        sharePhoto(state.detail)
                        navigateToDownloadWallpaper(
                            raw = state.detail?.rawQuality,
                            high = state.detail?.highQuality,
                            medium = state.detail?.mediumQuality,
                            low = state.detail?.lowQuality,
                            imageTitle = state.detail?.desc
                        )
                        addOrDeleteFavorites(state.detail)
                    })
                }

                false -> requireView().showToast(requireContext(), R.string.internet_error)

                null -> {}
            }
        }
    }

    private fun addOrDeleteFavorites(photo: Photo?) {
        binding.toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewModel.handleUIEvent(
                    DetailScreenEvent.AddFavorites(photo)
                )
            } else {
                viewModel.handleUIEvent(
                    DetailScreenEvent.DeleteFavorites(photo)
                )
            }
        }
    }

    private fun showProfileInfos(photo: Photo?) {
        binding.buttonInfo.setOnClickListener {
            navigateWithDirection(
                DetailFragmentDirections.toProfile(
                    imageUrl = photo?.profileimage,
                    profileUrl = photo?.unsplashProfile,
                    name = photo?.name,
                    bio = photo?.bio,
                    location = photo?.location,
                    username = photo?.username
                )
            )
        }
    }

    private fun navigateToSetWallpaper(imageUrl: String?) {
        binding.textViewSetWallpaper.setOnClickListener {
            navigateWithDirection(DetailFragmentDirections.toSetWallpaper(imageUrl = imageUrl))
        }
    }

    private fun navigateToDownloadWallpaper(
        raw: String?,
        high: String?,
        medium: String?,
        low: String?,
        imageTitle: String?
    ) {
        binding.buttonDownload.setOnClickListener {
            navigateWithDirection(
                DetailFragmentDirections.toDownload(
                    raw = raw,
                    high = high,
                    medium = medium,
                    low = low,
                    imageTitle = imageTitle
                )
            )
        }
    }

    private fun setItems(photo: Photo?) {
        with(binding) {
            imageViewPhotoOwner.load(photo?.profileimage ?: "") {
                diskCachePolicy(CachePolicy.DISABLED)
                transformations(CircleCropTransformation())
                placeholder(R.drawable.avatar)
                allowConversionToBitmap(true)
            }

            imageViewDetail.load(photo?.urls ?: "") {
                diskCachePolicy(CachePolicy.DISABLED)
                placeholder(
                    requireContext().itemLoading(resources.getColor(R.color.background_main_icon))
                )
                allowConversionToBitmap(true)
            }

            toolbar.title = photo?.desc ?: ""
            textViewPhotoOwnerName.text = photo?.username ?: ""
            textViewPhotoOwnerPortfolio.text = photo?.portfolio ?: ""
            textViewViewsCount.text = photo?.views?.toFormattedString() ?: ""
            textViewDownloadsCount.text = photo?.downloads?.toFormattedString() ?: ""
            textViewLikeCount.text = photo?.likes?.toFormattedString()
            textViewCreateTimeValue.text = photo?.createdAt?.formatDate()
        }
    }

    private fun sharePhoto(photo: Photo?) {
        binding.buttonShare.setOnClickListener {
            val share = Intent.createChooser(
                Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, photo?.urls)
                },
                null
            )
            startActivity(share)
        }
    }

    private fun favoriteCheck(id: String?) {
        lifecycleScope.launch {
            viewModel.favorites.collectLatest { result ->
                result.favorites.forEach {
                    if (it != null) {
                        if (it.id == id) {
                            binding.toggleButton.isChecked = it.isChecked
                        }
                    }
                }
            }
        }
    }
}
