package com.oguzdogdu.wallies.presentation.detail

import android.graphics.Color
import androidx.core.graphics.toColor
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.oguzdogdu.domain.model.singlephoto.Photo
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentDetailBinding
import com.oguzdogdu.wallies.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    @Inject
    lateinit var connection : CheckConnection

    private val viewModel: DetailViewModel by viewModels()

    private val args: DetailFragmentArgs by navArgs()

    override fun initListeners() {
        super.initListeners()
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

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

                            } else {
                                DialogHelper.showInternetCheckDialog(requireContext()){
                                    args.id?.let { viewModel.getSinglePhoto(it) }
                                }
                            }
                        }
                    }
                    else -> setItems(result.detail)
                }
            }.observeInLifecycle(this@DetailFragment)
        }
    }

    private fun setItems(photo: Photo?){
        with(binding) {

            imageViewPhotoOwner.load(photo?.profileimage ?: "") {
                diskCachePolicy(CachePolicy.DISABLED)
                transformations(CircleCropTransformation())
                placeholder(R.drawable.avatar)
                allowConversionToBitmap(true)
            }

            imageViewDetail.load(photo?.urls ?: "") {
                diskCachePolicy(CachePolicy.DISABLED)
                placeholder(requireContext().itemLoading())
                allowConversionToBitmap(true)
            }

            textViewImageDesc.text = photo?.desc ?: ""
            textViewPhotoOwnerName.text = photo?.username ?: ""
            textViewPhotoOwnerPortfolio.text = photo?.portfolio ?: ""
            textViewViewsCount.text = photo?.views?.toFormattedString() ?: ""
            textViewDownloadsCount.text = photo?.downloads?.toFormattedString() ?: ""
            textViewLikeCount.text = photo?.likes?.toFormattedString()
            textViewCreateTimeValue.text = photo?.createdAt?.formatDate()
        }
    }
}