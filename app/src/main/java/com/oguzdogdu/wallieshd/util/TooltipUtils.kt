package com.oguzdogdu.wallieshd.util

import android.content.Context
import android.view.Gravity
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.oguzdogdu.wallieshd.R
import com.skydoves.balloon.*

class TooltipUtils(private val context: Context) : ITooltipUtils {
    override fun getDefaultTooltip(lifecycle: LifecycleOwner, message: String): Balloon {
        return createBalloon(context) {
            setWidthRatio(0.65f)
            setHeight(BalloonSizeSpec.WRAP)
            setText(message)
            setTextColorResource(R.color.black)
            setTextSize(12f)
            setTextGravity(Gravity.START)
            setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
            setArrowSize(10)
            setArrowPosition(0.5f)
            setPadding(12)
            setCornerRadius(8f)
            setBackgroundColorResource(R.color.light_gray)
            setBalloonAnimation(BalloonAnimation.CIRCULAR)
            setLifecycleOwner(lifecycle)
            build()
        }
    }
}

fun ITooltipUtils.information(
    description: String,
    view: View,
    lifecycle: LifecycleOwner,
    direction: TooltipDirection
) {
    when (direction) {
        TooltipDirection.RIGHT -> {
            getDefaultTooltip(lifecycle, description).showAlignRight(view)
        }
        TooltipDirection.TOP -> {
            getDefaultTooltip(lifecycle, description).showAlignTop(view)
        }
        TooltipDirection.LEFT -> {
            getDefaultTooltip(lifecycle, description).showAlignLeft(view)
        }
        TooltipDirection.BOTTOM -> {
            getDefaultTooltip(lifecycle, description).showAlignBottom(view)
        }
    }
}

enum class TooltipDirection(val value: Int) {
    TOP(1), BOTTOM(2), LEFT(3), RIGHT(4)
}
