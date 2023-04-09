package com.oguzdogdu.wallies.util

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.oguzdogdu.wallies.R

fun RecyclerView.addItemDivider(context: Context) {
    val itemDivider = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
    itemDivider.setDrawable(
        GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(
                ContextCompat.getColor(
                    context,
                    R.color.background
                )
            )
            setSize(
                resources.getDimensionPixelSize(R.dimen.dp_1),
                resources.getDimensionPixelSize(R.dimen.dp_1)
            )
        }
    )
    this.addItemDecoration(itemDivider)
}

fun View.showSnackMessage(
    message: String?,
    length: Int = Snackbar.LENGTH_SHORT,
) {
    message?.let {
        try {
            val snack = Snackbar.make(this, it, length)
            snack.show()
        } catch (ex: Exception) {
            print(ex)
        }
    }
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.hide() {
    visibility = View.GONE
}