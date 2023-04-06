package com.oguzdogdu.wallies.util

import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.oguzdogdu.wallies.databinding.DialogInternetCheckBinding

object DialogHelper {
    fun showInternetCheckDialog(context: Context, handler: () -> Unit) {
        val dialogBuilder = MaterialAlertDialogBuilder(context)
        val layoutInflater = LayoutInflater.from(context)
        val binding = DialogInternetCheckBinding.inflate(layoutInflater)
        dialogBuilder.setView(binding.root)

        val alertDialog = dialogBuilder.create().apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(false)
        }

        alertDialog.show()

        binding.btnOk.setOnClickListener {
            alertDialog.dismiss()
            handler()
            alertDialog.hide()
        }
    }
}