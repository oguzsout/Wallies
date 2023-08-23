package com.oguzdogdu.wallies.presentation.profiledetail

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.oguzdogdu.domain.model.userdetail.UserDetails
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentProfileDetailBinding
import com.oguzdogdu.wallies.util.hide
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.setupRecyclerView
import com.oguzdogdu.wallies.util.show
import com.oguzdogdu.wallies.util.showToast
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
                is ProfileDetailState.UserDetailError -> {
                    state.errorMessage?.let {
                        requireView().showToast(
                            requireContext(),
                            it,
                            Toast.LENGTH_LONG
                        )
                    }
                }

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
                is ProfileDetailState.UserCollectionsError -> {
                    state.errorMessage?.let {
                        requireView().showToast(
                            requireContext(),
                            it,
                            Toast.LENGTH_LONG
                        )
                    }
                }

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
    }
}
