package com.oguzdogdu.wallies.presentation.detail

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.oguzdogdu.domain.model.favorites.FavoriteImages
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    @Inject
    lateinit var connection : CheckConnection

    private val viewModel: DetailViewModel by viewModels()

    private val args: DetailFragmentArgs by navArgs()

    override fun initListeners() {
        super.initListeners()
        binding.toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            lifecycleScope.launch {
                viewModel.photo.onEach { result ->
                    if (isChecked) {
                        viewModel.addImagesToFavorites(
                            FavoriteImages(
                                id = result.detail?.id ?: "",
                                url = result.detail?.urls ?: "",
                                profileImage = result.detail?.profileimage ?: "",
                                portfolioUrl = result.detail?.portfolio ?: "",
                                name = result.detail?.username ?: "",
                                isChecked = true
                            )
                        )
                    } else {
                        viewModel.deleteImagesToFavorites(
                            FavoriteImages(
                                id = result.detail?.id ?: "",
                                url = result.detail?.urls ?: "",
                                profileImage = result.detail?.profileimage ?: "",
                                portfolioUrl = result.detail?.portfolio ?: "",
                                name = result.detail?.username ?: "",
                                isChecked = false
                            )
                        )
                    }

                }.observeInLifecycle(this@DetailFragment)
            }

            binding.toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun observeData() {
        super.observeData()
        args.id?.let { viewModel.getSinglePhoto(it)}
        checkConnection()
    }

    private fun checkConnection(){
        connection.observe(this@DetailFragment) { isConnected ->
            when (isConnected) {
                true -> {
                    getDetailScreenData()
                }
                false -> {
                    requireView().showToast(requireContext(), R.string.internet_error)
                }
                null -> {}
            }
        }
    }

    private fun getDetailScreenData(){
        lifecycleScope.launch {
            viewModel.photo.onEach { result ->
                when {
                    result.isLoading -> {

                    }
                    result.error.isNotEmpty() -> {

                    }
                    else -> {
                        setItems(result.detail)
                        favoriteCheck(result.detail?.id)
                        showProfileInfos(result.detail)
                    }
                }
            }.observeInLifecycle(this@DetailFragment)
        }
    }

    private fun showProfileInfos(photo: Photo?){
        binding.buttonInfo.setOnClickListener {
            val arguments = Bundle().apply {
                putString("imageUrl", photo?.profileimage)
                putString("profileUrl", photo?.unsplashProfile)
                putString("name", photo?.name)
                putString("bio", photo?.bio)
                putString("location", photo?.location)
            }
            navigate(R.id.toProfile, arguments)
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
                placeholder(requireContext().itemLoading(resources.getColor(R.color.background_main_icon)))
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



