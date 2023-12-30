package com.oguzdogdu.wallieshd.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.model.home.HomePopularAndLatest
import com.oguzdogdu.domain.usecase.auth.GetCurrentUserInfoUseCase
import com.oguzdogdu.domain.usecase.home.GetPopularAndLatestUseCase
import com.oguzdogdu.domain.usecase.topics.GetTopicsListUseCase
import com.oguzdogdu.domain.wrapper.onFailure
import com.oguzdogdu.domain.wrapper.onLoading
import com.oguzdogdu.domain.wrapper.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrentUserInfoUseCase: GetCurrentUserInfoUseCase,
    private val getTopicsListUseCase: GetTopicsListUseCase,
    private val getPopularAndLatestHomeListUseCase: GetPopularAndLatestUseCase
) : ViewModel() {

    private val _userState = MutableStateFlow(MainScreenState.UserProfile(""))
    val userState = _userState.asStateFlow()

    private val _homeListState = MutableStateFlow<HomeRecyclerViewItems?>(null)
    val homeListState = _homeListState.asStateFlow()

    fun handleUIEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.FetchMainScreenUserData -> {
                getUserProfileImage()
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
                        value.onLoading {
                            _homeListState.update {
                                HomeRecyclerViewItems.PopularImageList(
                                    loading = true
                                )
                            }
                        }
                        value.onSuccess { list ->
                            _homeListState.update {
                                HomeRecyclerViewItems.PopularImageList(
                                    loading = false,
                                    list = list
                                )
                            }
                        }
                        value.onFailure { error ->
                            _homeListState.update {
                                HomeRecyclerViewItems.PopularImageList(
                                    error = error
                                )
                            }
                        }
                    }

                    HomePopularAndLatest.ListType.LATEST.type -> {
                        value.onLoading {
                            _homeListState.update {
                                HomeRecyclerViewItems.LatestImageList(
                                    loading = true
                                )
                            }
                        }

                        value.onSuccess { list ->
                            _homeListState.update {
                                HomeRecyclerViewItems.LatestImageList(
                                    loading = false,
                                    list = list
                                )
                            }
                        }

                        value.onFailure { error ->
                            _homeListState.update {
                                HomeRecyclerViewItems.LatestImageList(
                                    error = error
                                )
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
                value.onLoading {
                    _homeListState.update {
                        HomeRecyclerViewItems.TopicsTitleList(
                            loading = true
                        )
                    }
                }
                value.onSuccess { list ->
                    _homeListState.update {
                        HomeRecyclerViewItems.TopicsTitleList(
                            loading = false,
                            topics = list
                        )
                    }
                }
                value.onFailure { error ->
                    _homeListState.update {
                        HomeRecyclerViewItems.TopicsTitleList(
                            error = error
                        )
                    }
                }
            }
        }
    }

    private fun getUserProfileImage() {
        viewModelScope.launch {
            getCurrentUserInfoUseCase.invoke().collectLatest { value ->
                value.onLoading {}

                value.onSuccess { user ->
                    _userState.update {
                        MainScreenState.UserProfile(
                            profileImage = user?.image
                        )
                    }
                }

                value.onFailure {
                }
            }
        }
    }
}
