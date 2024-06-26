package com.oguzdogdu.wallieshd.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.oguzdogdu.wallieshd.util.loadImage
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.setFragmentResultListener
import com.oguzdogdu.wallieshd.util.setupRecyclerView
import com.oguzdogdu.wallieshd.util.show
import com.oguzdogdu.wallieshd.util.toFormattedString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    private val viewModel: DetailViewModel by viewModels()

    private var photo: Photo? = null

    private val args: DetailFragmentArgs by navArgs()

    private val tagsAdapter by lazy { TagsAdapter() }

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        binding.apply {
            recyclerViewTags.setupRecyclerView(
                layout = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                ),
                adapter = tagsAdapter,
                true,
                onScroll = {}
            )
        }
    }

    override fun initListeners() {
        super.initListeners()
        binding.toolbar.setNavigationOnClickListener {
            navigateBack()
        }
        tagsAdapter.setOnItemClickListener {
            val tag = Bundle().apply {
                putString("tag", it)
            }
            navigate(R.id.toSearch, extras = tag)
        }
        addOrDeleteFavorites()
    }

    override fun observeData() {
        super.observeData()
        viewModel.handleUIEvent(DetailScreenEvent.GetPhotoDetails(id = args.id))
        viewModel.handleUIEvent(
            DetailScreenEvent.GetPhotoFromWhere(
                id = args.id.orEmpty()
            )
        )
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
                }

                is DetailState.DetailOfPhoto -> {
                    photo = state.detail
                    binding.dashboardContainer.show()
                    setItems()
                    showProfileInfos()
                    navigateToSetWallpaper()
                    sharePhoto()
                    navigateToDownloadWallpaper()
                }
                is DetailState.UserAuthenticated -> {
                    when (state.isAuthenticated) {
                        false -> navigateWithDirection(
                            DetailFragmentDirections.toSavedPlaceWarning()
                        )
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
                        viewModel.handleUIEvent(DetailScreenEvent.CheckUserAuth)
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
            val bundle = Bundle().apply {
                this.putString("username", photo?.username)
            }
            navigate(R.id.toProfileDetail, bundle)
        }
    }

    private fun navigateToSetWallpaper() {
        val displayMetrics = DisplayMetrics()
        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        binding.textViewSetWallpaper.setOnClickListener {
            navigateWithDirection(
                DetailFragmentDirections.toSetWallpaper(
                    imageUrl = photo?.rawQuality + "&w=$width&h=$height$FIT$AUTO"
                )
            )
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
            }

            imageViewDetail.loadImage(
                photo?.urls.orEmpty(),
                placeholder = requireContext()
                    .itemLoading(resources.getColor(R.color.background_main_icon))
            )
            toolbar.title = photo?.desc.orEmpty()
            textViewPhotoOwnerName.text = photo?.username.orEmpty()
            textViewPhotoOwnerPortfolio.text = photo?.portfolio.orEmpty()
            textViewViewsCount.text = photo?.views?.toFormattedString().orEmpty()
            textViewDownloadsCount.text = photo?.downloads?.toFormattedString().orEmpty()
            textViewLikeCount.text = photo?.likes?.toFormattedString().orEmpty()
            textViewCreateTimeValue.text = photo?.createdAt?.formatDate().orEmpty()
            tagsAdapter.submitList(photo?.tag)
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
    companion object {
        private const val FIT = "&fit=facearea"
        private const val AUTO = "&auto=enhance"
    }
}
