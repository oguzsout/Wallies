package com.oguzdogdu.wallieshd.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.auth.CheckUserAuthenticatedUseCase
import com.oguzdogdu.domain.usecase.auth.GetCurrentUserDatasUseCase
import com.oguzdogdu.domain.usecase.latest.GetLatestUseCase
import com.oguzdogdu.domain.usecase.popular.GetPopularUseCase
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
    private val getPopularUseCase: GetPopularUseCase,
    private val getLatestUseCase: GetLatestUseCase
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
                                topics = value.data
                            )
                        }
                    }
                    is Resource.Loading -> {}
                    is Resource.Error -> {}
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
