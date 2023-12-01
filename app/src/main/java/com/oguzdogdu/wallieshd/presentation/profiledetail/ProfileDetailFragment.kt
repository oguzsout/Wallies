package com.oguzdogdu.wallieshd.presentation.profiledetail

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.google.android.material.tabs.TabLayoutMediator
import com.oguzdogdu.domain.model.userdetail.UserDetails
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.core.snackbar.MessageType
import com.oguzdogdu.wallieshd.databinding.FragmentProfileDetailBinding
import com.oguzdogdu.wallieshd.presentation.profiledetail.usercollections.UserCollectionsFragment
import com.oguzdogdu.wallieshd.presentation.profiledetail.userphotos.UserPhotosFragment
import com.oguzdogdu.wallieshd.util.hide
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileDetailFragment : BaseFragment<FragmentProfileDetailBinding>(
    FragmentProfileDetailBinding::inflate
) {

    private val viewModel: ProfileDetailViewModel by activityViewModels()

    private val fragments =
        listOf(UserPhotosFragment(), UserCollectionsFragment())

    private val tabTitles = listOf("Photos", "Collections")

    private val args: ProfileDetailFragmentArgs by navArgs()

    override fun initViews() {
        super.initViews()
        viewModel.setUsername(args.username)
        initViewPager()
        initTabLayout()
        binding.toolbar.setLeftIcon(R.drawable.back)
    }

    override fun initListeners() {
        super.initListeners()
        binding.toolbar.setLeftIconClickListener {
            navigateBack()
        }
    }

    override fun observeData() {
        super.observeData()
        getUserDetails()
    }

    private fun getUserDetails() {
        viewModel.handleUIEvent(ProfileDetailEvent.FetchUserDetailInfos)
        viewModel.getUserDetails.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when (state) {
                is ProfileDetailState.Loading -> {}
                is ProfileDetailState.UserDetailError -> showMessage(
                    message = state.errorMessage.orEmpty(),
                    type = MessageType.ERROR
                )
                is ProfileDetailState.UserInfos -> {
                    setUserDatas(userDetails = state.userDetails)
                }
                else -> {}
            }
        })
    }

    private fun setUserDatas(userDetails: UserDetails?) {
        binding.apply {
            textViewPortfolioUrl.text = userDetails?.portfolioUrl.orEmpty()
            textViewNumberOfPosts.text = userDetails?.postCount?.toString()
            textViewNumberOfFollowers.text = userDetails?.followersCount?.toString()
            textViewNumberOfFollowing.text = userDetails?.followingCount?.toString()
            textViewBio.text = userDetails?.bio.orEmpty()
            imageViewDetailProfilePhoto.load(userDetails?.profileImage) {
                diskCachePolicy(CachePolicy.DISABLED)
                transformations(CircleCropTransformation())
                placeholder(R.drawable.ic_default_avatar)
                allowConversionToBitmap(true)
            }
            userDetails?.username?.let {
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
    private fun initTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun initViewPager() {
        val pagerAdapter = UserScreensViewPagerAdapter(
            requireParentFragment().requireActivity(),
            fragments
        )
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.isUserInputEnabled = true
    }
}
