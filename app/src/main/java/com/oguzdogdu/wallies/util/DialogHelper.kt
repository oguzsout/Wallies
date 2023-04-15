package com.oguzdogdu.wallies.util

import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.databinding.DialogInternetCheckBinding

object DialogHelper {
    fun showInternetCheckDialog(
        context: Context,
        title: String?,
        @StringRes message: Int?,
        @StringRes positiveButtonText: Int?,
        negativeButtonText: String? = null,
        @DrawableRes icon: Int?,
        handler: () -> Unit,
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message.orEmpty())
            .setPositiveButton(positiveButtonText.orEmpty()) { dialog, which ->
                dialog.dismiss()
                handler()
                dialog.dismiss()
            }
            .setNegativeButton(negativeButtonText) { dialog, which ->
                // İptal düğmesine tıklandığında yapılacak işlemler
            }
            .setIcon(icon.orEmpty())
            .setCancelable(false)
            .show()
    }
}