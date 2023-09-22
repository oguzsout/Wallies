package com.oguzdogdu.wallieshd.core.snackbar

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import coil.load
import com.google.android.material.snackbar.ContentViewCallback
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.databinding.InfoSnackbarBinding
import com.oguzdogdu.wallieshd.util.hide
import com.oguzdogdu.wallieshd.util.show

class CustomSnackBarView(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr), ContentViewCallback {

    private var binding = InfoSnackbarBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        clipToPadding = true
    }

    fun setupView(type: MessageType, message: String, additionalMessage: String?) {
        SnackbarModel(type, message, additionalMessage)
        additionalMessage?.let {
            binding.textViewAdditionalMessage.show()
        }

        when (type) {
            MessageType.SUCCESS -> {
                prepareViews(
                    message,
                    additionalMessage,
                    R.drawable.ic_completed,
                    R.color.green,
                    R.color.white
                )
            }
            MessageType.ERROR -> {
                prepareViews(
                    message,
                    additionalMessage,
                    R.drawable.ic_cancel,
                    R.color.red,
                    R.color.white
                )
            }
        }

        binding.imageViewIcon.setOnClickListener { binding.root.isVisible = false }
    }

    private fun prepareViews(
        message: String?,
        additionalMessage: String?,
        @DrawableRes drawable: Int,
        @ColorRes backgroundColor: Int,
        @ColorRes contentColor: Int
    ) {
        binding.imageViewIcon.load(drawable)
        binding.container.backgroundTintList =
            ContextCompat.getColorStateList(context, backgroundColor)
        if (additionalMessage == null) {
            binding.textViewAdditionalMessage.hide()
        } else {
            binding.textViewAdditionalMessage.show()
        }
        binding.textViewMessage.text = message
        val resolvedColor = ContextCompat.getColor(context, contentColor)
        binding.imageViewIcon.setColorFilter(resolvedColor, PorterDuff.Mode.SRC_IN)
        binding.textViewAdditionalMessage.setTextColor(resolvedColor)
        binding.textViewMessage.setTextColor(resolvedColor)
    }

    override fun animateContentIn(delay: Int, duration: Int) {
        TODO("Not yet implemented")
    }

    override fun animateContentOut(delay: Int, duration: Int) {
        TODO("Not yet implemented")
    }
}
