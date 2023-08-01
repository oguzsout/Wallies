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
import com.oguzdogdu.wallies.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileDetailFragment : BaseFragment<FragmentProfileDetailBinding>(
    FragmentProfileDetailBinding::inflate
) {

    private val viewModel: ProfileDetailViewModel by viewModels()

    private val args: ProfileDetailFragmentArgs by navArgs()

    private val usersPhotosAdapter by lazy { UsersPhotosAdapter() }

    override fun observeData() {
        super.observeData()
        viewModel.getUserDetails(username = args.username)
        viewModel.getUsersPhotos(username = args.username)
        viewModel.getUserDetails.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when {
                state.loading -> {}
                state.errorMessage?.isNotEmpty() == true -> {
                    requireView().showToast(requireContext(), state.errorMessage, Toast.LENGTH_LONG)
                }

                else -> setUserDatas(userDetails = state.userDetails)
            }
        })
        viewModel.getUsersPhotos.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when {
                state.loading -> {}
                state.errorMessage?.isNotEmpty() == true -> {
                    requireView().showToast(requireContext(), state.errorMessage, Toast.LENGTH_LONG)
                }

                state.usersPhotos.isNotEmpty() -> usersPhotosAdapter.submitList(state.usersPhotos)
            }
        })
    }

    override fun initViews() {
        super.initViews()
        binding.rvUserPhotos.setupRecyclerView(
            layoutManager = GridLayoutManager(requireContext(), 2),
            adapter = usersPhotosAdapter,
            true,
            onScroll = {}
        )
    }

    override fun initListeners() {
        super.initListeners()
        binding.toolbar.setNavigationOnClickListener {
            navigateBack()
        }
        usersPhotosAdapter.setOnItemClickListener {
            navigateWithDirection(ProfileDetailFragmentDirections.toDetail(it?.id))
        }
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
            toolbar.title = userDetails?.name
        }
    }
}
