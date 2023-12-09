package com.oguzdogdu.wallieshd.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.model.home.HomePopularAndLatest
import com.oguzdogdu.domain.usecase.auth.CheckUserAuthenticatedUseCase
import com.oguzdogdu.domain.usecase.auth.GetCurrentUserDatasUseCase
import com.oguzdogdu.domain.usecase.home.GetPopularAndLatestUseCase
import com.oguzdogdu.domain.usecase.topics.GetTopicsListUseCase
import com.oguzdogdu.domain.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrentUserDatasUseCase: GetCurrentUserDatasUseCase,
    private val userAuthenticatedUseCase: CheckUserAuthenticatedUseCase,
    private val getTopicsListUseCase: GetTopicsListUseCase,
    private val getPopularAndLatestHomeListUseCase: GetPopularAndLatestUseCase
) : ViewModel() {

    private val _userState = MutableStateFlow<MainScreenState?>(null)
    val userState = _userState.asStateFlow()

    private val _homeListState = MutableStateFlow<HomeRecyclerViewItems?>(null)
    val homeListState = _homeListState.asStateFlow()

    fun handleUIEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.FetchMainScreenUserData -> {
                getUserProfileImage()
                checkUserAuthenticate()
            }

            is MainScreenEvent.FetchMainScreenList -> {
                fetchTopicTitleList()
                fetchPopularAndLatestList(event.type)
            }
        }
    }
    private fun fetchPopularAndLatestList(type: String?) {
        viewModelScope.launch {
            getPopularAndLatestHomeListUseCase.invoke(type = type).collect { value ->
                when (type) {
                    HomePopularAndLatest.ListType.POPULAR.type -> {
                        when (value) {
                            is Resource.Success -> {
                                _homeListState.update {
                                    HomeRecyclerViewItems.PopularImageList(
                                        loading = false,
                                        list = value.data
                                    )
                                }
                            }

                            is Resource.Loading -> {
                                _homeListState.update {
                                    HomeRecyclerViewItems.PopularImageList(
                                        loading = true
                                    )
                                }
                            }

                            is Resource.Error -> {
                                _homeListState.update {
                                    HomeRecyclerViewItems.PopularImageList(
                                        error = value.errorMessage
                                    )
                                }
                            }
                        }
                    }

                    HomePopularAndLatest.ListType.LATEST.type -> {
                        when (value) {
                            is Resource.Success -> {
                                _homeListState.update {
                                    HomeRecyclerViewItems.LatestImageList(
                                        loading = false,
                                        list = value.data
                                    )
                                }
                            }

                            is Resource.Loading -> {
                                _homeListState.update {
                                    HomeRecyclerViewItems.LatestImageList(
                                        loading = true
                                    )
                                }
                            }

                            is Resource.Error -> {
                                _homeListState.update {
                                    HomeRecyclerViewItems.LatestImageList(
                                        error = value.errorMessage
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun fetchTopicTitleList() {
        viewModelScope.launch {
            getTopicsListUseCase.invoke().collectLatest { value ->
                when (value) {
                    is Resource.Success -> {
                        _homeListState.update {
                            HomeRecyclerViewItems.TopicsTitleList(
                                loading = false,
                                topics = value.data
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _homeListState.update {
                            HomeRecyclerViewItems.TopicsTitleList(
                                loading = true
                            )
                        }
                    }
                    is Resource.Error -> {
                        _homeListState.update {
                            HomeRecyclerViewItems.TopicsTitleList(
                                error = value.errorMessage
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getUserProfileImage() {
        viewModelScope.launch {
            getCurrentUserDatasUseCase.invoke().collectLatest { value ->
                when (value) {
                    is Resource.Success -> {
                        _userState.update {
                            MainScreenState.UserProfile(
                                profileImage = value.data.image
                            )
                        }
                    }
                    is Resource.Loading -> {}
                    is Resource.Error -> {}
                }
            }
        }
    }
    private fun checkUserAuthenticate() {
        viewModelScope.launch {
            userAuthenticatedUseCase.invoke().collectLatest { status ->
                when (status) {
                    is Resource.Success -> {
                        _userState.update {
                            MainScreenState.UserAuthenticated(isAuthenticated = status.data)
                        }
                    }
                    is Resource.Loading -> {}
                    is Resource.Error -> {}
                }
            }
        }
    }
}
