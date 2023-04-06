package com.oguzdogdu.wallies.presentation.detail

import android.graphics.Bitmap
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.ImageLoader
import coil.load
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.oguzdogdu.domain.model.singlephoto.Photo
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentDetailBinding
import com.oguzdogdu.wallies.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    @Inject
    lateinit var connection : CheckConnection

    private val viewModel: DetailViewModel by viewModels()

    private val args: DetailFragmentArgs by navArgs()

    override fun observeData() {
        super.observeData()
        args.id?.let { viewModel.getSinglePhoto(it) }
        lifecycleScope.launchWhenStarted {
            viewModel.photo.onEach { result ->
                when {
                    result.isLoading -> {

                    }
                    result.error.isNotEmpty() -> {
                        connection.observe(this@DetailFragment) { isConnected ->
                            if (isConnected) {
                                args.id?.let { viewModel.getSinglePhoto(it) }
                            } else {
                                requireView().showSnackMessage("Check Connectivity")
                            }
                        }
                    }
                    else -> setItems(result.detail)
                }
            }.observeInLifecycle(this@DetailFragment)
        }
    }

    private fun setItems(photo: Photo?){
        Glide.with(this)
            .load(photo?.urls)
            .placeholder(R.drawable.placeholder)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.imageViewDetail)
        with(binding) {
            imageViewPhotoOwner.load(photo?.profileimage ?: "") {
                diskCachePolicy(CachePolicy.DISABLED)
                transformations(CircleCropTransformation())
                placeholder(R.drawable.avatar)
                allowConversionToBitmap(true)
            }
            textViewPhotoOwnerName.text = photo?.username ?: ""
            textViewPhotoOwnerPortfolio.text = photo?.portfolio ?: ""
            textViewViewsCount.text = photo?.views?.toPrettyString() ?: ""
            textViewDownloadsCount.text = photo?.downloads?.toString() ?: ""
            textViewLikeCount.text = photo?.likes.toString() ?: ""
        }
    }
}