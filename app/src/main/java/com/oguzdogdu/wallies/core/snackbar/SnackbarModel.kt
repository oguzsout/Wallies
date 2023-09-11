package com.oguzdogdu.wallies.core.snackbar

data class SnackbarModel(
    val type: MessageType,
    val message: String,
    val additionalMessage: String?
)
