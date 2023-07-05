package com.oguzdogdu.wallies.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
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

inline fun RecyclerView.setupRecyclerView(
    layoutManager: RecyclerView.LayoutManager,
    adapter: RecyclerView.Adapter<*>,
    hasFixedSize: Boolean = true,
    itemAnimator: RecyclerView.ItemAnimator? = null,
    itemDecoration: RecyclerView.ItemDecoration? = null,
    crossinline onScroll: () -> Unit
) {
    this.layoutManager = layoutManager
    this.adapter = adapter
    setHasFixedSize(hasFixedSize)
    itemAnimator?.let { this.itemAnimator = it }
    itemDecoration?.let { addItemDecoration(it) }
    onScroll()
}

@SuppressLint("SuspiciousIndentation", "ResourceType")
fun View.showSnackMessage(
    @StringRes buttonText: Int?,
    @StringRes message: Int?,
    length: Int = Snackbar.LENGTH_SHORT
) {
    message?.let {
        try {
            val snack = Snackbar.make(this.rootView, it, length)
            snack.setAnchorView(R.id.bottomNavigationView)
            snack.setAction(buttonText.orEmpty()) {
                snack.dismiss()
            }
            snack.show()
        } catch (ex: Exception) {
            print(ex)
        }
    }
}

fun View.showToast(context: Context, @StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, duration).show()
}

fun View.showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, duration).show()
}

fun Context.itemLoading(@ColorInt color: Int?): Drawable {
    val circularProgressDrawable = CircularProgressDrawable(this).apply {
        strokeWidth = 7f
        centerRadius = 40f
        setColorSchemeColors(color.orEmpty())
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
