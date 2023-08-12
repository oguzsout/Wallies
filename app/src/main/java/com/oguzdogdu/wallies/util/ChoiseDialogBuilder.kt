package com.oguzdogdu.wallies.util

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.oguzdogdu.wallies.R

object ChoiseDialogBuilder {
    fun choiseAnyValueIntoDialogList(
        context: Context,
        title: String? = null,
        selectedValue: String? = null,
        list: Array<String>? = null,
        handlerList: (DialogInterface, Int) -> Unit,
        positive: (DialogInterface, Int) -> Unit
    ) {
        val dialogBuilder = MaterialAlertDialogBuilder(context).setTitle(title)
            .setSingleChoiceItems(list, list?.indexOf(selectedValue)!!) { dialog, which ->
                handlerList.apply {
                    invoke(dialog, which)
                }
            }
            .setPositiveButton(context.getString(R.string.ok)) { dialog, which ->
                positive.apply {
                    invoke(dialog, which)
                }
            }
            .setNegativeButton(context.getString(R.string.cancel)) { dialog, which -> }
            .setCancelable(false)

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}
