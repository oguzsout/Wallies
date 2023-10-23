package com.oguzdogdu.wallieshd.presentation.detail

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.oguzdogdu.domain.model.singlephoto.Photo
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.core.snackbar.MessageType
import com.oguzdogdu.wallieshd.databinding.FragmentDetailBinding
import com.oguzdogdu.wallieshd.util.formatDate
import com.oguzdogdu.wallieshd.util.hide
import com.oguzdogdu.wallieshd.util.itemLoading
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.show
import com.oguzdogdu.wallieshd.util.toFormattedString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

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
        showDetailScreenDatas()
        viewModel.handleUIEvent(DetailScreenEvent.GetPhotoDetails(id = args.id))
    }

    private fun showDetailScreenDatas() {
        viewModel.photo.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when (state) {
                is DetailState.DetailError -> showMessage(
                    message = state.errorMessage.orEmpty(),
                    type = MessageType.ERROR
                )

                is DetailState.Loading -> binding.dashboardContainer.hide()

                is DetailState.FavoriteStateOfPhoto -> {
                    binding.toggleButton.isChecked = state.favorite
                }

                is DetailState.DetailOfPhoto -> {
                    binding.dashboardContainer.show()
                    viewModel.handleUIEvent(
                        DetailScreenEvent.GetPhotoFromWhere(
                            id = state.detail?.id.orEmpty()
                        )
                    )
                    setItems(state.detail)
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
                }

                else -> {}
            }
        })
    }

    private fun addOrDeleteFavorites(photo: Photo?) {
        binding.toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewModel.toggleState.observeInLifecycle(viewLifecycleOwner, observer = {
                    if (!it) {
                        viewModel.handleUIEvent(
                            DetailScreenEvent.AddFavorites(photo)
                        )
                    }
                })
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
                DetailFragmentDirections.toProfileDetail(
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
            imageViewPhotoOwner.load(photo?.profileimage.orEmpty()) {
                diskCachePolicy(CachePolicy.DISABLED)
                transformations(CircleCropTransformation())
                placeholder(R.drawable.avatar)
                allowConversionToBitmap(true)
            }

            imageViewDetail.load(photo?.urls.orEmpty()) {
                diskCachePolicy(CachePolicy.DISABLED)
                placeholder(
                    requireContext().itemLoading(resources.getColor(R.color.background_main_icon))
                )
                allowConversionToBitmap(true)
            }

            toolbar.title = photo?.desc.orEmpty()
            textViewPhotoOwnerName.text = photo?.username.orEmpty()
            textViewPhotoOwnerPortfolio.text = photo?.portfolio.orEmpty()
            textViewViewsCount.text = photo?.views?.toFormattedString().orEmpty()
            textViewDownloadsCount.text = photo?.downloads?.toFormattedString().orEmpty()
            textViewLikeCount.text = photo?.likes?.toFormattedString().orEmpty()
            textViewCreateTimeValue.text = photo?.createdAt?.formatDate().orEmpty()
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
}
