package com.oguzdogdu.wallieshd.presentation.profiledetail

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.oguzdogdu.domain.model.userdetail.UserDetails
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.core.snackbar.MessageType
import com.oguzdogdu.wallieshd.databinding.FragmentProfileDetailBinding
import com.oguzdogdu.wallieshd.util.hide
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.orEmpty
import com.oguzdogdu.wallieshd.util.setupRecyclerView
import com.oguzdogdu.wallieshd.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileDetailFragment : BaseFragment<FragmentProfileDetailBinding>(
    FragmentProfileDetailBinding::inflate
) {

    private val viewModel: ProfileDetailViewModel by viewModels()

    private val args: ProfileDetailFragmentArgs by navArgs()

    private val usersPhotosAdapter by lazy { UsersPhotosAdapter() }

    override fun initViews() {
        super.initViews()
        binding.rvUserPhotos.setupRecyclerView(
            layout = GridLayoutManager(requireContext(), 2),
            adapter = usersPhotosAdapter,
            true,
            onScroll = {}
        )
        binding.toolbar.setLeftIcon(R.drawable.back)
    }

    override fun initListeners() {
        super.initListeners()
        binding.toolbar.setLeftIconClickListener {
            navigateBack()
        }
        usersPhotosAdapter.setOnItemClickListener {
            navigateWithDirection(ProfileDetailFragmentDirections.toDetail(it?.id))
        }
    }

    override fun observeData() {
        super.observeData()
        getUserDetails()
        getUserCollections()
    }

    private fun getUserDetails() {
        viewModel.handleUIEvent(ProfileDetailEvent.FetchUserDetailInfos(username = args.username))
        viewModel.getUserDetails.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when (state) {
                is ProfileDetailState.Loading -> binding.progressBar.show()
                is ProfileDetailState.UserDetailError -> showMessage(
                    message = state.errorMessage.orEmpty(),
                    type = MessageType.ERROR
                )
                is ProfileDetailState.UserInfos -> {
                    binding.progressBar.hide()
                    setUserDatas(userDetails = state.userDetails)
                }
                else -> {}
            }
        })
    }

    private fun getUserCollections() {
        viewModel.handleUIEvent(ProfileDetailEvent.FetchUserCollections(username = args.username))
        viewModel.getUserDetails.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when (state) {
                is ProfileDetailState.Loading -> binding.progressBar.show()
                is ProfileDetailState.UserCollectionsError -> showMessage(
                    message = state.errorMessage.orEmpty(),
                    type = MessageType.ERROR
                )
                is ProfileDetailState.UserCollections -> {
                    binding.progressBar.hide()
                    usersPhotosAdapter.submitList(state.usersPhotos)
                }
                else -> {}
            }
        })
    }

    private fun setUserDatas(userDetails: UserDetails?) {
        binding.apply {
            textViewPortfolioUrl.text = userDetails?.portfolioUrl
            textViewNumberOfPosts.text = userDetails?.postCount?.toString()
            textViewNumberOfFollowers.text = userDetails?.followersCount?.toString()
            textViewNumberOfFollowing.text = userDetails?.followingCount?.toString()
            textViewUsername.text = userDetails?.name
            textViewBio.text = userDetails?.bio
            imageViewDetailProfilePhoto.load(userDetails?.profileImage) {
                diskCachePolicy(CachePolicy.DISABLED)
                transformations(CircleCropTransformation())
                placeholder(R.drawable.ic_default_avatar)
                allowConversionToBitmap(true)
            }
            userDetails?.name?.let {
                binding.toolbar.setTitle(
                    title = it,
                    titleStyleRes = R.style.DialogTitleText
                )
            }
        }
        checkUserActivityCount(userDetails)
    }

    private fun checkUserActivityCount(userDetails: UserDetails?) {
        when (userDetails?.postCount != null && userDetails.followersCount != null && userDetails.followingCount != null) {
            true -> {
                binding.textViewPosts.show()
                binding.textViewFollowers.show()
                binding.textViewFollowing.show()
            }
            false -> {
                binding.textViewPosts.hide()
                binding.textViewFollowers.hide()
                binding.textViewFollowing.hide()
            }
        }
    }
}
