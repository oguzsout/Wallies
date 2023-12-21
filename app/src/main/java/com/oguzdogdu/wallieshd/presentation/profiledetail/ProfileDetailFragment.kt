package com.oguzdogdu.wallieshd.presentation.profiledetail

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.oguzdogdu.domain.model.userdetail.UserDetails
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.core.snackbar.MessageType
import com.oguzdogdu.wallieshd.databinding.FragmentProfileDetailBinding
import com.oguzdogdu.wallieshd.presentation.collections.POWER_MENU_PERCENTAGE
import com.oguzdogdu.wallieshd.presentation.profiledetail.usercollections.UserCollectionsFragment
import com.oguzdogdu.wallieshd.presentation.profiledetail.userphotos.UserPhotosFragment
import com.oguzdogdu.wallieshd.util.ITooltipUtils
import com.oguzdogdu.wallieshd.util.colorize
import com.oguzdogdu.wallieshd.util.hide
import com.oguzdogdu.wallieshd.util.loadImage
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.show
import com.skydoves.powermenu.CircularEffect
import com.skydoves.powermenu.CustomPowerMenu
import com.skydoves.powermenu.MenuAnimation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileDetailFragment : BaseFragment<FragmentProfileDetailBinding>(
    FragmentProfileDetailBinding::inflate
) {

    private val args: ProfileDetailFragmentArgs by navArgs()

    private val viewModel: ProfileDetailViewModel by activityViewModels()

    private val fragments by lazy {
        listOf(UserPhotosFragment(), UserCollectionsFragment())
    }

    @Inject
    lateinit var tooltip: ITooltipUtils

    private lateinit var userDetails: UserDetails

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        initViewPager()
        binding.toolbar.setLeftIcon(R.drawable.back)
    }

    override fun initListeners() {
        super.initListeners()
        binding.toolbar.setLeftIconClickListener {
            navigateBack()
        }
        binding.textViewPortfolioUrl.setOnClickListener {
            adjustFilteredView(
                view = it,
                instagram = userDetails.instagram,
                twitter = userDetails.twitter,
                portfolio = userDetails.portfolio
            )
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.setUsername(args.username)
        viewModel.handleUIEvent(ProfileDetailEvent.FetchUserDetailInfos)
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
                    state.userDetails?.let {
                        userDetails = it
                    }
                }
                else -> {}
            }
        })
    }

    private fun adjustFilteredView(
        view: View,
        instagram: String?,
        twitter: String?,
        portfolio: String?
    ) {
        val items = arrayListOf<UserSocialAccountsMenu>()

        instagram?.let {
            items.add(
                UserSocialAccountsMenu(
                    title = "Instagram",
                    titleIcon = resources.getDrawable(R.drawable.icons8_instagram, null),
                    UserSocialAccountsMenu.MenuItemType.INSTAGRAM
                )
            )
        }

        twitter?.let {
            items.add(
                UserSocialAccountsMenu(
                    title = "Twitter",
                    titleIcon = resources.getDrawable(R.drawable.icons8_twitterx, null),
                    UserSocialAccountsMenu.MenuItemType.TWITTER
                )
            )
        }

        portfolio?.let {
            items.add(
                UserSocialAccountsMenu(
                    title = "Portfolio",
                    titleIcon = resources.getDrawable(R.drawable.portfolio_svgrepo_com, null),
                    UserSocialAccountsMenu.MenuItemType.PORTFOLIO
                )
            )
        }

        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val width = screenWidth * POWER_MENU_PERCENTAGE

        val customPowerMenu = CustomPowerMenu.Builder(requireContext(), UserAccountsMenuAdapter())
            .addItemList(items)
            .setWidth(width.toInt())
            .setAnimation(MenuAnimation.SHOW_UP_CENTER)
            .setBackgroundAlpha(0.5f)
            .build()

        customPowerMenu.setOnMenuItemClickListener { position, item ->
            when (item.menuItemType) {
                UserSocialAccountsMenu.MenuItemType.INSTAGRAM -> {
                    if (instagram != null) {
                        openInstagramProfile(instagram)
                    }
                }
                UserSocialAccountsMenu.MenuItemType.TWITTER -> {
                    if (twitter != null) {
                        openTwitterProfile(twitter)
                    }
                }

                UserSocialAccountsMenu.MenuItemType.PORTFOLIO -> {
                    if (portfolio != null) {
                        openPortfolioUrl(portfolio)
                    }
                }
            }
        }

        customPowerMenu.circularEffect = CircularEffect.BODY
        customPowerMenu.setAutoDismiss(true)
        customPowerMenu.showAsDropDown(view)
    }

    private fun openInstagramProfile(username: String) {
        val url = "https://instagram.com/$username"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun openTwitterProfile(username: String) {
        val url = "https://twitter.com/$username"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun openPortfolioUrl(url: String) {
        openUrl(url)
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun setUserDatas(userDetails: UserDetails?) {
        binding.apply {
            val fullText = getString(R.string.connect_with_user, userDetails?.username)
            val coloredText = fullText.colorize(
                ContextCompat.getColor(requireContext(), R.color.purple_03)
            )
            textViewPortfolioUrl.text = coloredText
            textViewNumberOfPosts.text = userDetails?.postCount?.toString()
            textViewNumberOfFollowers.text = userDetails?.followersCount?.toString()
            textViewNumberOfFollowing.text = userDetails?.followingCount?.toString()
            textViewUserNameAndLastname.text = userDetails?.name.orEmpty()
            textViewLocation.text = userDetails?.location.orEmpty()
            textViewBio.text = userDetails?.bio.orEmpty()
            imageViewDetailProfilePhoto.loadImage(
                url = userDetails?.profileImage,
                placeholder = resources.getDrawable(R.drawable.ic_default_avatar, null),
                radius = true
            )

            userDetails?.username?.let {
                binding.toolbar.setTitle(
                    title = it,
                    titleStyleRes = R.style.DialogTitleText
                )
            }
            initTabLayout(
                userPhotoSize = userDetails?.totalPhotos,
                userCollectionSize = userDetails?.totalCollections
            )
        }
        when (userDetails?.location.isNullOrEmpty()) {
            true -> {
                binding.textViewLocation.hide()
            }
            false -> {
                binding.textViewLocation.show()
            }
        }
        when (userDetails?.bio.isNullOrEmpty()) {
            true -> {
                binding.textViewBio.hide()
            }
            false -> {
                binding.textViewBio.show()
            }
        }
        checkUserActivityCount(userDetails)
    }

    private fun checkUserActivityCount(userDetails: UserDetails?) {
        when (
            userDetails?.postCount != null &&
                userDetails.followersCount != null &&
                userDetails.followingCount != null
        ) {
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
    private fun initTabLayout(userPhotoSize: Int?, userCollectionSize: Int?) {
        val tabTitles =
            listOf(
                "${getString(R.string.photos)} (${(userPhotoSize)})",
                "${getString(R.string.collections_title)} (${(userCollectionSize)})"
            )

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
