package com.oguzdogdu.wallieshd.presentation.authenticateduser.editnameandsurname

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.auth.ChangeSurnameUseCase
import com.oguzdogdu.domain.usecase.auth.ChangeUserNameUseCase
import com.oguzdogdu.domain.usecase.auth.GetCurrentUserDatasUseCase
import com.oguzdogdu.domain.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EditUsernameSurnameViewModel @Inject constructor(
    private val getCurrentUserDatasUseCase: GetCurrentUserDatasUseCase,
    private val changeUserNameUseCase: ChangeUserNameUseCase,
    private val changeSurnameUseCase: ChangeSurnameUseCase
) : ViewModel() {

    private val _userState: MutableStateFlow<EditUsernameSurnameScreenState?> = MutableStateFlow(
        null
    )
    val userState = _userState.asStateFlow()

    init {
        fetchAuthenticatedUserInfo()
    }

    fun handleUIEvent(event: EditUsernameSurnameEvent) {
        when (event) {
            is EditUsernameSurnameEvent.ChangedUserName -> {
                changeUsername(event.name)
            }
            is EditUsernameSurnameEvent.ChangedSurName -> {
                changeSurname(event.surname)
            }
        }
    }

    private fun fetchAuthenticatedUserInfo() {
        viewModelScope.launch {
            getCurrentUserDatasUseCase.invoke().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> _userState.update { EditUsernameSurnameScreenState.Loading }

                    is Resource.Error -> _userState.update {
                        EditUsernameSurnameScreenState.UserInfoError(
                            result.errorMessage
                        )
                    }

                    is Resource.Success -> _userState.update {
                        EditUsernameSurnameScreenState.UserInfos(
                            name = result.data.name,
                            surname = result.data.surname,
                            email = result.data.email,
                            profileImage = result.data.image
                        )
                    }
                }
            }
        }
    }

    private fun changeUsername(name: String?) {
        viewModelScope.launch {
            changeUserNameUseCase.invoke(name = name)
        }
    }

    private fun changeSurname(surname: String?) {
        viewModelScope.launch {
            changeSurnameUseCase.invoke(surname = surname)
        }
    }
}
