package com.oguzdogdu.wallies.presentation.authenticateduser.editprofilephoto

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.oguzdogdu.data.common.Constants
import com.oguzdogdu.domain.usecase.auth.ChangeUserProfilePhotoUseCase
import com.oguzdogdu.domain.usecase.auth.GetCurrentUserDatasUseCase
import com.oguzdogdu.domain.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

@HiltViewModel
class EditProfilePhotoViewModel @Inject constructor(
    private val getCurrentUserDatasUseCase: GetCurrentUserDatasUseCase,
    private val changeUserProfilePhotoUseCase: ChangeUserProfilePhotoUseCase
) : ViewModel() {

    private val _userImageState: MutableStateFlow<EditProfilePhotoScreenState?> = MutableStateFlow(
        null
    )
    val userImageState = _userImageState.asStateFlow()

    init {
        fetchUserImage()
    }

    fun handleUiEvents(event: EditProfilePhotoEvent) {
        when (event) {
            is EditProfilePhotoEvent.ChangeProfileImage -> {
                changeProfileImage(uri = event.photoUri)
            }
        }
    }

    private fun fetchUserImage() {
        viewModelScope.launch {
            getCurrentUserDatasUseCase.invoke().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> _userImageState.update { EditProfilePhotoScreenState.Loading }

                    is Resource.Error -> _userImageState.update {
                        EditProfilePhotoScreenState.UserInfoError(
                            result.errorMessage
                        )
                    }

                    is Resource.Success -> _userImageState.update {
                        EditProfilePhotoScreenState.ProfileImage(
                            image = result.data.image
                        )
                    }
                }
            }
        }
    }

    private fun changeProfileImage(uri: Uri?) {
        viewModelScope.launch {
            changeUserProfilePhotoUseCase.invoke(photo = uploadImage(uri = uri))
            checkChangedPhotoStatus(uri = uri)
        }
    }

    private fun checkChangedPhotoStatus(uri: Uri?) {
        viewModelScope.launch {
            if (uploadImage(uri)?.isNotEmpty() == true) {
                _userImageState.update {
                    EditProfilePhotoScreenState.ProcessCompleted(
                        isCompleted = true
                    )
                }
            } else {
                _userImageState.update {
                    EditProfilePhotoScreenState.ProcessCompleted(
                        isCompleted = false
                    )
                }
            }
        }
    }

    private suspend fun uploadImage(uri: Uri?): String? = suspendCancellableCoroutine { continuation ->
        if (uri == null) {
            continuation.resume(null)
            return@suspendCancellableCoroutine
        }

        val storageRef = FirebaseStorage.getInstance().reference.child(Constants.IMAGE)
        val childRef = storageRef.child(System.currentTimeMillis().toString())

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
    }
}
