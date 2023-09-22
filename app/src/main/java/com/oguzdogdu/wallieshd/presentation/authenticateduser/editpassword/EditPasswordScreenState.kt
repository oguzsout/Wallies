package com.oguzdogdu.wallieshd.presentation.authenticateduser.editpassword

import androidx.annotation.StringRes

sealed class EditPasswordScreenState {
    object Loading : EditPasswordScreenState()
    data class PasswordChangeError(val errorMessage: String?) : EditPasswordScreenState()
    data class PasswordChangeSucceed(@StringRes val successMessage: Int?) : EditPasswordScreenState()
}
