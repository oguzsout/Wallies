package com.oguzdogdu.wallies.presentation.signup

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.oguzdogdu.data.common.Constants
import com.oguzdogdu.domain.usecase.auth.SignUpUseCase
import com.oguzdogdu.domain.wrapper.Resource
import com.oguzdogdu.wallies.util.FieldValidators
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

@HiltViewModel
class SignUpViewModel @Inject constructor(private val signUpUseCase: SignUpUseCase) : ViewModel() {

    private val _signUpState: MutableStateFlow<SignUpState> = MutableStateFlow(SignUpState.Start)
    val signUpState = _signUpState.asStateFlow()

    private val userEmail = MutableStateFlow("")

    private val userPassword = MutableStateFlow("")

    fun handleUIEvent(event: SignUpScreenEvent) {
        when (event) {
            is SignUpScreenEvent.ButtonState -> {
                buttonStateUpdate()
            }
            is SignUpScreenEvent.UserInfosForSignUp -> userSignUp(
                name = event.name,
                surname = event.surname,
                email = event.email,
                password = event.password,
                photoUri = event.photoUri
            )
        }
    }

    fun setEmail(email: String?) {
        email?.let {
            userEmail.value = it
        }
    }

    fun setPassword(password: String?) {
        password?.let {
            userPassword.value = it
        }
    }

    private fun checkButtonState(): Boolean {
        return FieldValidators.isValidEmailCheck(input = userEmail.value) && FieldValidators.isValidPasswordCheck(
            input = userPassword.value
        )
    }

    private fun buttonStateUpdate() {
        viewModelScope.launch {
            val state = checkButtonState()
            _signUpState.update { SignUpState.ButtonEnabled(isEnabled = state) }
        }
    }

    private fun userSignUp(
        name: String?,
        surname: String?,
        email: String?,
        password: String?,
        photoUri: Uri?
    ) {
        viewModelScope.launch {
            signUpUseCase.invoke(
                user = com.oguzdogdu.domain.model.auth.User(
                    name = name.orEmpty(),
                    surname = surname.orEmpty(),
                    email = email.orEmpty(),
                    image = uploadImage(photoUri).orEmpty()
                ),
                password = password.orEmpty()
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _signUpState.update { SignUpState.Loading }
                    }

                    is Resource.Error -> {
                        _signUpState.update { SignUpState.ErrorSignUp(result.errorMessage) }
                    }

                    is Resource.Success -> {
                        _signUpState.update { SignUpState.UserSignUp }
                    }
                }
            }
        }
    }

    private suspend fun uploadImage(uri: Uri?): String? = suspendCancellableCoroutine { continuation ->
        val storageRef = FirebaseStorage.getInstance().reference.child(Constants.IMAGE)
        val childRef = storageRef.child(System.currentTimeMillis().toString())

        uri?.let { uri ->
            val uploadTask = childRef.putFile(uri)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { exception ->
                        throw exception
                    }
                }
                childRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    continuation.resume(downloadUri?.toString())
                } else {
                    continuation.resume(null)
                }
            }

            continuation.invokeOnCancellation {
                uploadTask.cancel()
            }
        } ?: run {
            continuation.resume(null)
        }
    }
}
