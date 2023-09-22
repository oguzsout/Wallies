package com.oguzdogdu.wallieshd.core.snackbar

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.util.findSuitableParent

class CustomSnackbarImpl(
    parent: ViewGroup,
    customView: CustomSnackBarView
) : BaseTransientBottomBar<CustomSnackbarImpl>(parent, customView, customView) {

    init {
        setupView()
    }

    companion object {
        fun build(
            fragment: Fragment,
            type: MessageType,
            message: String,
            additionalMessage: String?
        ): CustomSnackbarImpl {
            val view = fragment.activity?.window?.decorView?.findViewById(android.R.id.content) as View
            val parent = view.findSuitableParent()
                ?: throw IllegalArgumentException(
                    "No suitable parent found from the given view. Please provide a valid view."
                )
            val customView = CustomSnackBarView(view.context)
            customView.setupView(type, message, additionalMessage)
            return CustomSnackbarImpl(parent, customView)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun setupView() {
        view.setBackgroundColor(ContextCompat.getColor(view.context, android.R.color.transparent))
        val spacing = view.context.resources.getDimensionPixelSize(R.dimen.dp_16)
        val layoutParams = (view.layoutParams as FrameLayout.LayoutParams).apply {
            setMargins(spacing, 0, spacing, spacing)
        }
        layoutParams.also { view.layoutParams = it }
    }
}
