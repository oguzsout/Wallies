package com.oguzdogdu.wallies.presentation.profiledetail

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.oguzdogdu.domain.model.userdetail.UserDetails
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentProfileDetailBinding
import com.oguzdogdu.wallies.util.hide
import com.oguzdogdu.wallies.util.observeInLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileDetailFragment : BaseFragment<FragmentProfileDetailBinding>(
    FragmentProfileDetailBinding::inflate
) {
    private val viewModel: ProfileDetailViewModel by viewModels()
    private val args: ProfileDetailFragmentArgs by navArgs()
    override fun initViews() {
        super.initViews()
    }

    override fun initListeners() {
        super.initListeners()
        binding.toolbar.setNavigationOnClickListener {
            navigateBack()
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.getUserDetails(username = args.username)
        viewModel.getUserDetails.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            setUserDatas(userDetails = state.userDetails)
        })
    }

    private fun setUserDatas(userDetails: UserDetails?) {
        binding.apply {
            when {
                userDetails?.postCount.toString().isBlank() -> textViewPosts.hide()
                userDetails?.followersCount.toString().isBlank() -> textViewFollowers.hide()
                userDetails?.followingCount.toString().isBlank() -> textViewFollowing.hide()
            }
            imageViewDetailProfilePhoto.load(userDetails?.profileImage) {
                diskCachePolicy(CachePolicy.DISABLED)
                transformations(CircleCropTransformation())
                placeholder(R.drawable.ic_not_found_user_image)
                allowConversionToBitmap(true)
            }
            toolbar.title = userDetails?.name
            textViewPortfolioUrl.text = userDetails?.portfolioUrl

            textViewNumberOfPosts.text = userDetails?.postCount.toString()
            textViewNumberOfFollowers.text = userDetails?.followersCount.toString()
            textViewNumberOfFollowing.text = userDetails?.followingCount.toString()
            textViewUsername.text = userDetails?.name
            textViewBio.text = userDetails?.bio
        }
    }
}
