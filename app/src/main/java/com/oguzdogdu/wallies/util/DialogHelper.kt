package com.oguzdogdu.wallies.util

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogHelper {
    fun showInternetCheckDialog(
        context: Context,
        title: String?,
        @StringRes message: Int?,
        @StringRes positiveButtonText: Int?,
        @DrawableRes icon: Int?,
        handler: () -> Unit
    ) {
        val dialogBuilder = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message.orEmpty())
            .setPositiveButton(positiveButtonText.orEmpty()) { dialog, which ->
                handler().apply {
                    dialog.dismiss()
                }
            }
            .setIcon(icon.orEmpty())
            .setCancelable(false)

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}
