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
import com.oguzdogdu.wallieshd.presentation.detail.SavedPlaceWarningDialog.Companion.PROCESS_KEY
import com.oguzdogdu.wallieshd.presentation.detail.SavedPlaceWarningDialog.Companion.RESULT_EXTRA_KEY
import com.oguzdogdu.wallieshd.util.formatDate
import com.oguzdogdu.wallieshd.util.hide
import com.oguzdogdu.wallieshd.util.itemLoading
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.setFragmentResultListener
import com.oguzdogdu.wallieshd.util.show
import com.oguzdogdu.wallieshd.util.toFormattedString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    private val viewModel: DetailViewModel by viewModels()

    private var photo: Photo? = null

    private val args: DetailFragmentArgs by navArgs()

    override fun initListeners() {
        super.initListeners()
        binding.toolbar.setNavigationOnClickListener {
            navigateBack()
        }
        addOrDeleteFavorites()
    }

    override fun observeData() {
        super.observeData()
        viewModel.handleUIEvent(DetailScreenEvent.GetPhotoDetails(id = args.id))
        showDetailScreenDatas()
    }

    override fun onResume() {
        super.onResume()
        this.parentFragment?.setFragmentResultListener(requestKey = PROCESS_KEY) { key, bundle ->
            val result = bundle.getBoolean(RESULT_EXTRA_KEY)
            viewModel.handleUIEvent(
                DetailScreenEvent.SetLoginDialogState(
                    isShown = result
                )
            )
        }
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
                    viewModel.handleUIEvent(DetailScreenEvent.SetLoginDialogState(state.favorite))
                }

                is DetailState.DetailOfPhoto -> {
                    photo = state.detail
                    binding.dashboardContainer.show()
                    viewModel.handleUIEvent(
                        DetailScreenEvent.GetPhotoFromWhere(
                            id = state.detail?.id.orEmpty()
                        )
                    )
                    setItems()
                    showProfileInfos()
                    navigateToSetWallpaper()
                    sharePhoto()
                    navigateToDownloadWallpaper()
                }
                is DetailState.StateOfLoginDialog -> {
                    when (state.isShown) {
                        false -> navigate(R.id.toSavedPlaceWarning, null)
                        true -> {}
                    }
                }

                else -> {}
            }
        })
    }

    private fun addOrDeleteFavorites() {
        binding.toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewModel.toggleState.observeInLifecycle(viewLifecycleOwner, observer = { result ->
                    if (!result) {
                        viewModel.handleUIEvent(DetailScreenEvent.SetLoginDialogState(false))
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

    private fun showProfileInfos() {
        binding.buttonInfo.setOnClickListener {
            navigateWithDirection(
                DetailFragmentDirections.toProfileDetail(
                    username = photo?.username
                )
            )
        }
    }

    private fun navigateToSetWallpaper() {
        binding.textViewSetWallpaper.setOnClickListener {
            navigateWithDirection(DetailFragmentDirections.toSetWallpaper(imageUrl = photo?.urls))
        }
    }

    private fun navigateToDownloadWallpaper() {
        binding.buttonDownload.setOnClickListener {
            navigateWithDirection(
                DetailFragmentDirections.toDownload(
                    raw = photo?.rawQuality,
                    high = photo?.highQuality,
                    medium = photo?.mediumQuality,
                    low = photo?.lowQuality,
                    imageTitle = photo?.desc
                )
            )
        }
    }

    private fun setItems() {
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

    private fun sharePhoto() {
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
