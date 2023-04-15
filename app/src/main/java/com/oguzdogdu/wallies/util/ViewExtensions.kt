package com.oguzdogdu.wallies.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.snackbar.Snackbar
import com.oguzdogdu.wallies.R


fun RecyclerView.addItemDivider() {
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

fun Context.itemLoading() : Drawable {
    val circularProgressDrawable = CircularProgressDrawable(this).apply {
        strokeWidth = 7f
        centerRadius = 40f
        setColorSchemeColors(Color.parseColor("#ff8c42"))
        start()
    }
    return circularProgressDrawable
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