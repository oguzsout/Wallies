package com.oguzdogdu.wallies.presentation.profiledetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.userdetails.GetUnsplashUserDetailsUseCase
import com.oguzdogdu.domain.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val userDetailsUseCase: GetUnsplashUserDetailsUseCase
) : ViewModel() {

    private val _getUserDetails: MutableStateFlow<ProfileDetailState> = MutableStateFlow(
        ProfileDetailState()
    )
    val getUserDetails = _getUserDetails.asStateFlow()

    fun getUserDetails(username: String?) {
        viewModelScope.launch {
            userDetailsUseCase.invoke(username).collectLatest { infos ->
                when (infos) {
                    is Resource.Success -> _getUserDetails.update {
                        it.copy(
                            loading = false,
                            userDetails = infos.data
                        )
                    }
                    is Resource.Loading -> _getUserDetails.update { it.copy(loading = true) }
                    is Resource.Error -> _getUserDetails.update {
                        it.copy(
                            errorMessage = infos.errorMessage
                        )
                    }
                }
            }
        }
    }
}
