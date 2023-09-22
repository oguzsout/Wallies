package com.oguzdogdu.wallieshd.util

import androidx.lifecycle.LifecycleOwner
import com.skydoves.balloon.Balloon

interface ITooltipUtils {
    fun getDefaultTooltip(lifecycle: LifecycleOwner, message: String): Balloon
}
