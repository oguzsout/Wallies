package com.oguzdogdu.wallies.util

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.oguzdogdu.wallies.R

class Toolbar @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attributeSet, defStyleAttr) {

    @DrawableRes
    private var leftIcon: Int? = null

    @ColorRes
    private var leftIconTint: Int? = null

    private var title: String? = null

    @ColorRes
    private var titleColor: Int? = null

    var onLeftClickListener: OnLeftClickListener? = null

    init {
        initializeLayout()
        readAttributes(attributeSet)
        setupToolbar()
    }

    private fun readAttributes(attributeSet: AttributeSet?) {
        if (attributeSet == null) {
            removeAllViews()
            return
        }

        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.Toolbar)
        try {
            if (attrs.hasValue(R.styleable.Toolbar_toolbar_left_icon)) {
                leftIcon = attrs.getResourceId(R.styleable.Toolbar_toolbar_left_icon, -1)
            }

            if (attrs.hasValue(R.styleable.Toolbar_toolbar_left_icon_tint)) {
                leftIconTint = attrs.getResourceId(R.styleable.Toolbar_toolbar_left_icon_tint, -1)
            }

            if (attrs.hasValue(R.styleable.Toolbar_toolbar_title)) {
                title = attrs.getString(R.styleable.Toolbar_toolbar_title)
            }

            if (attrs.hasValue(R.styleable.Toolbar_toolbar_title_color)) {
                titleColor = attrs.getResourceId(
                    R.styleable.Toolbar_toolbar_title_color,
                    R.color.white
                )
            }
        } finally {
            attrs.recycle()
        }
    }

    private fun initializeLayout() {
        orientation = HORIZONTAL
    }

    private fun setupToolbar() {
        val leftLayout = LinearLayoutCompat(context).apply {
            orientation = HORIZONTAL
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
            tag = TAG_LEFT_LAYOUT
        }

        if (leftIcon != null) {
            val leftButton = buildLeftButton()
            leftButton.icon = ContextCompat.getDrawable(context, leftIcon!!)

            leftButton.setOnClickListener {
                onLeftClickListener?.onLeftButtonClick()
            }

            if (leftIconTint != null) {
                leftButton.setIconTintResource(leftIconTint!!)
            }
            leftLayout.addView(leftButton)
        }

        if (title != null) {
            val titleTextView = buildTitleTextView()
            titleTextView.text = title
            leftLayout.addView(titleTextView)
        }

        addView(leftLayout)

        if (title != null) {
            setPadding(0, 0, 0, resources.getDimensionPixelSize(R.dimen.dp_12))
        }
    }

    private fun buildLeftButton(): MaterialButton {
        return MaterialButton(context).apply {
            iconSize = resources.getDimensionPixelSize(R.dimen.dp_24)
            tag = TAG_LEFT_BUTTON

            val typedValue = TypedValue()
            context.theme.resolveAttribute(
                android.R.attr.selectableItemBackgroundBorderless,
                typedValue,
                true
            )
            setBackgroundResource(typedValue.resourceId)

            minWidth = 0
            minimumWidth = 0
            minimumHeight = 0
            minHeight = 0
            val padding = resources.getDimensionPixelSize(R.dimen.dp_8)
            setPadding(padding, padding, padding, padding)
        }
    }

    private fun buildTitleTextView(): MaterialTextView {
        return MaterialTextView(
            android.view.ContextThemeWrapper(context, R.style.Title1Medium),
            null,
            0
        ).apply {
            titleColor?.let { color ->
                setTextColor(ContextCompat.getColor(context, color))
            }
            maxLines = 2
            ellipsize = TextUtils.TruncateAt.END
            tag = TAG_TEXT_VIEW_TITLE
            val padding = resources.getDimensionPixelSize(R.dimen.dp_8)
            setPadding(padding, padding, 0, 0)
        }
    }

    private fun refreshLayout() {
        val leftLayout = findViewWithTag<LinearLayoutCompat>(TAG_LEFT_LAYOUT)

        if (!this.title.isNullOrEmpty()) {
            if (findViewWithTag<MaterialTextView>(TAG_TEXT_VIEW_TITLE) == null) {
                val titleTextView = buildTitleTextView()
                titleTextView.text = this.title
                titleTextView.setTextColor(ContextCompat.getColor(context, titleColor!!))

                if (findViewWithTag<MaterialButton>(TAG_LEFT_BUTTON) == null) {
                    leftLayout.addView(titleTextView, 0)
                } else {
                    leftLayout.addView(titleTextView, 1)
                }
            } else {
                val titleTextView = findViewWithTag<MaterialTextView>(TAG_TEXT_VIEW_TITLE)
                titleTextView.text = this.title
            }
        }

        if (title != null) {
            setPadding(0, 0, 0, resources.getDimensionPixelSize(R.dimen.dp_16))
        }
    }

    fun setTitle(title: String?) {
        this.title = title
        refreshLayout()
    }

    fun setTitleColor(@ColorRes colorRes: Int) {
        this.titleColor = colorRes
        refreshLayout()
    }

    interface OnLeftClickListener {
        fun onLeftButtonClick()
    }

    companion object {
        const val TAG_LEFT_LAYOUT = "leftLayout"
        const val TAG_TEXT_VIEW_TITLE = "textViewTitle"
        const val TAG_LEFT_BUTTON = "leftButton"
    }
}
