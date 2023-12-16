package com.oguzdogdu.wallieshd.presentation.main

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.oguzdogdu.domain.model.home.HomePopularAndLatest
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.core.snackbar.MessageType
import com.oguzdogdu.wallieshd.databinding.FragmentMainBinding
import com.oguzdogdu.wallieshd.util.hide
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.setupRecyclerView
import com.oguzdogdu.wallieshd.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val viewModel: MainViewModel by viewModels()

    private val topicsTitleAdapter by lazy { TopicsTitleAdapter() }

    private val homePopularAdapter by lazy { HomePopularAdapter() }

    private val homeLatestAdapter by lazy { HomeLatestAdapter() }

    override fun firstExecution(savedInstanceState: Bundle?) {
        super.firstExecution(savedInstanceState)
        viewModel.handleUIEvent(MainScreenEvent.FetchMainScreenUserData)
        viewModel.handleUIEvent(
            MainScreenEvent.FetchMainScreenList(HomePopularAndLatest.ListType.POPULAR.type)
        )
        viewModel.handleUIEvent(
            MainScreenEvent.FetchMainScreenList(HomePopularAndLatest.ListType.LATEST.type)
        )
    }

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
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
            topicsViewContainer.rvHome.setupRecyclerView(
                layout = GridLayoutManager(
                    requireContext(),
                    2
                ),
                adapter = topicsTitleAdapter,
                true,
                onScroll = {}
            )
            popularViewContainer.rvHome.setupRecyclerView(
                layout = LinearLayoutManager(
                    requireContext(),
                    RecyclerView.HORIZONTAL,
                    false
                ),
                adapter = homePopularAdapter,
                true,
                onScroll = {}
            )
            latestViewContainer.rvHome.setupRecyclerView(
                layout = LinearLayoutManager(
                    requireContext(),
                    RecyclerView.HORIZONTAL,
                    false
                ),
                adapter = homeLatestAdapter,
                true,
                onScroll = {}
            )
            topicsViewContainer.textViewTitle.text = getString(R.string.topics_title)
            topicsViewContainer.textViewShowAll.text = getString(R.string.show_all)
            popularViewContainer.textViewTitle.text = getString(R.string.popular_title)
            popularViewContainer.textViewShowAll.text = getString(R.string.show_all)
            latestViewContainer.textViewTitle.text = getString(R.string.latest_title)
            latestViewContainer.textViewShowAll.text = getString(R.string.show_all)
            textViewTitle.text = resources.getString(R.string.app_name)
            imageViewSearch.load(R.drawable.search)
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.userState.observeInLifecycle(viewLifecycleOwner, ::getAuthenticatedUserInfos)
        fetchHomeScreenList()
    }

    private fun fetchHomeScreenList() {
        viewModel.homeListState.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when (state) {
                is HomeRecyclerViewItems.TopicsTitleList -> handleTopicsTitleListState(state)
                is HomeRecyclerViewItems.PopularImageList -> getPopularImageList(
                    state
                )

                is HomeRecyclerViewItems.LatestImageList -> getLatestImageList(state)
                null -> {}
            }
        })
    }

    private fun getAuthenticatedUserInfos(state: MainScreenState?) {
        when (state) {
            is MainScreenState.UserProfile -> {
                when (state.profileImage.isNullOrBlank()) {
                    true -> binding.imageViewProfileAvatar.load(R.drawable.ic_default_avatar)

                    false -> binding.imageViewProfileAvatar.load(state.profileImage) {
                        transformations(CircleCropTransformation())
                    }
                }
            }

            is MainScreenState.UserAuthenticated -> {
                when (state.isAuthenticated) {
                    false -> binding.imageViewProfileAvatar.load(R.drawable.ic_default_avatar)
                    else -> {}
                }
                state.isAuthenticated?.let { goToUserInfoScreen(it) }
            }

            null -> return
        }
    }

    override fun initListeners() {
        super.initListeners()
        binding.imageViewSearch.setOnClickListener {
            navigate(R.id.toSearch, null)
        }
        topicsTitleAdapter.setOnItemClickListener { item ->
            navigateWithDirection(MainFragmentDirections.toTopicDetailList(item?.title))
        }
        homePopularAdapter.setOnItemClickListener {
            navigateWithDirection(MainFragmentDirections.toDetail(it?.id))
        }
        homeLatestAdapter.setOnItemClickListener {
            navigateWithDirection(MainFragmentDirections.toDetail(it?.id))
        }
        binding.topicsViewContainer.textViewShowAll.setOnClickListener {
            navigate(R.id.toTopics, null)
        }
        binding.popularViewContainer.textViewShowAll.setOnClickListener {
            navigate(R.id.toPopular, null)
        }
        binding.latestViewContainer.textViewShowAll.setOnClickListener {
            navigate(R.id.toLatest, null)
        }
    }

    private fun goToUserInfoScreen(isAuthenticated: Boolean) {
        binding.imageViewProfileAvatar.setOnClickListener {
            navigate(R.id.toAuthUser, bundleOf(Pair("auth", isAuthenticated)))
        }
    }

    private fun handleTopicsTitleListState(state: HomeRecyclerViewItems.TopicsTitleList) {
        when (state.loading) {
            true -> {
                binding.topicsViewContainer.progressBar.show()
            }

            false -> {
                binding.topicsViewContainer.progressBar.hide()
                topicsTitleAdapter.submitList(state.topics)
            }

            null -> {}
        }

        when {
            !state.error.isNullOrBlank() -> showMessage(
                message = state.error,
                type = MessageType.ERROR
            )

            else -> {}
        }
    }

    private fun getPopularImageList(state: HomeRecyclerViewItems.PopularImageList) {
        when (state.loading) {
            true -> {
                binding.popularViewContainer.progressBar.show()
            }

            false -> {
                binding.popularViewContainer.progressBar.hide()
                homePopularAdapter.submitList(state.list)
            }

            null -> {}
        }
        when {
            !state.error.isNullOrBlank() -> showMessage(
                message = state.error,
                type = MessageType.ERROR
            )

            else -> {}
        }
    }

    private fun getLatestImageList(state: HomeRecyclerViewItems.LatestImageList) {
        when (state.loading) {
            true -> {
                binding.latestViewContainer.progressBar.show()
            }

            false -> {
                binding.latestViewContainer.progressBar.hide()
                homeLatestAdapter.submitList(state.list)
            }

            null -> {}
        }
        when {
            !state.error.isNullOrBlank() -> showMessage(
                message = state.error,
                type = MessageType.ERROR
            )

            else -> {}
        }
    }
}
