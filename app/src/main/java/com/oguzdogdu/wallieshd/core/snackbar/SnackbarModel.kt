package com.oguzdogdu.wallieshd.core.snackbar

data class SnackbarModel(
    val type: MessageType,
    val message: String,
    val additionalMessage: String?
)
