package com.oguzdogdu.wallieshd.presentation.main

import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.core.snackbar.MessageType
import com.oguzdogdu.wallieshd.databinding.FragmentMainBinding
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.setupRecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val viewModel: MainViewModel by viewModels()

    private val topicsTitleAdapter by lazy { TopicsTitleAdapter() }

    override fun initViews() {
        super.initViews()
        with(binding) {
            nestedScrollMainContainer.setOnScrollChangeListener(
                NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                    if (scrollY > oldScrollY) {
                        (activity as MainActivity).slideDown()
                    } else if (scrollY < oldScrollY) {
                        (activity as MainActivity).slideUp()
                    }
                }
            )
            rvTopics.setupRecyclerView(
                layout = GridLayoutManager(requireContext(), 2),
                adapter = topicsTitleAdapter,
                true,
                onScroll = {}
            )
            includedLayout.textViewTitle.text = getString(R.string.topics_title)
            includedLayout.textViewShowAll.text = getString(R.string.show_all)
            textViewTitle.text = resources.getString(R.string.app_name)
            imageViewSearch.load(R.drawable.search)
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.handleUIEvent(MainScreenEvent.FetchMainScreenUserData)
        viewModel.handleUIEvent(MainScreenEvent.FetchMainScreenList)
        getAuthenticatedUserInfos()
        fetchHomeScreenList()
    }

    private fun fetchHomeScreenList() {
        viewModel.homeListState.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when (state) {
                is HomeRecyclerViewItems.TopicsTitleList -> {
                    topicsTitleAdapter.submitList(
                        state.topics
                    )
                }

                is HomeRecyclerViewItems.LatestImageList -> {}
                is HomeRecyclerViewItems.PopularImageList -> {}
                null -> {}
            }
        })
    }

    private fun getAuthenticatedUserInfos() {
        viewModel.userState.observeInLifecycle(viewLifecycleOwner, observer = { status ->
            when (status) {
                is MainScreenState.UserProfile -> {
                    when (status.profileImage.isNullOrBlank()) {
                        true -> binding.imageViewProfileAvatar.load(R.drawable.ic_default_avatar)

                        false -> binding.imageViewProfileAvatar.load(status.profileImage) {
                            diskCachePolicy(CachePolicy.DISABLED)
                            transformations(CircleCropTransformation())
                        }
                    }
                }

                is MainScreenState.UserAuthenticated -> {
                    when (status.isAuthenticated) {
                        false -> binding.imageViewProfileAvatar.load(R.drawable.ic_default_avatar)
                        else -> {}
                    }
                    status.isAuthenticated?.let { goToUserInfoScreen(it) }
                }

                else -> {}
            }
        })
    }

    override fun initListeners() {
        super.initListeners()
        binding.imageViewSearch.setOnClickListener {
            navigate(R.id.toSearch, null)
        }
        topicsTitleAdapter.setOnItemClickListener { item ->
            if (item != null) {
                showMessage(message = item.title!!, MessageType.SUCCESS)
            }
        }
    }

    private fun goToUserInfoScreen(isAuthenticated: Boolean) {
        binding.imageViewProfileAvatar.setOnClickListener {
            navigate(R.id.toAuthUser, bundleOf(Pair("auth", isAuthenticated)))
        }
    }
}
