package com.oguzdogdu.wallies.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
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

inline fun RecyclerView.setupRecyclerView(
    layout: RecyclerView.LayoutManager? = null,
    adapter: RecyclerView.Adapter<*>,
    hasFixedSize: Boolean = true,
    itemAnimator: RecyclerView.ItemAnimator? = null,
    itemDecoration: RecyclerView.ItemDecoration? = null,
    addDivider: Boolean = false,
    crossinline onScroll: () -> Unit
) {
    this.layoutManager = layout
    this.adapter = adapter
    setHasFixedSize(hasFixedSize)
    itemAnimator?.let { this.itemAnimator = it }
    itemDecoration?.let { addItemDecoration(it) }
    if (addDivider) {
        val itemDivider = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        itemDivider.setDrawable(
            GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(
                    ContextCompat.getColor(
                        context,
                        R.color.dim_gray
                    )
                )
                setSize(
                    resources.getDimensionPixelSize(R.dimen.dp_1),
                    resources.getDimensionPixelSize(R.dimen.dp_1)
                )
                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        val position = parent.getChildAdapterPosition(view)
                        if (position != parent.adapter!!.itemCount - 1) {
                            itemDivider.getItemOffsets(outRect, view, parent, state)
                        }
                    }
                })
            }
        )
    }
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
