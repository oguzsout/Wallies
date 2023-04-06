package com.oguzdogdu.wallies.util

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
                resources.getDimensionPixelSize(R.dimen.spacing_4xs),
                resources.getDimensionPixelSize(R.dimen.spacing_4xs)
            )
        }
    )
    this.addItemDecoration(itemDivider)
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