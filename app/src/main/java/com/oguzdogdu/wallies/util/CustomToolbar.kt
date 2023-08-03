package com.oguzdogdu.wallies.util

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.databinding.CustomToolbarBinding

class CustomToolbar @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private var binding = CustomToolbarBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        clipToPadding = true
        setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
    }

    fun setTitle(title: String, @StyleRes titleStyleRes: Int?) {
        binding.textViewToolbarTitle.text = title
        if (titleStyleRes != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.textViewToolbarTitle.setTextAppearance(titleStyleRes)
            } else {
                binding.textViewToolbarTitle.setTextColor(resources.getColor(R.color.black))
                val typeface = ResourcesCompat.getFont(context, R.font.googlesansmedium)
                binding.textViewToolbarTitle.typeface = typeface
                binding.textViewToolbarTitle.textSize = resources.getDimension(R.dimen.dp_16)
            }
        }
    }

    fun setLeftIcon(@DrawableRes iconRes: Int?) {
        if (iconRes != null) {
            binding.buttonLeft.show()
            binding.buttonLeft.setImageResource(iconRes)
        } else {
            binding.buttonLeft.hide()
        }
    }

    fun setLeftIconClickListener(listener: OnClickListener) {
        binding.buttonLeft.setOnClickListener(listener)
    }

    fun setRightIcon(@DrawableRes iconRes: Int) {
        binding.buttonRight.setImageResource(iconRes)
    }

    fun setRightIconClickListener(listener: OnClickListener) {
        binding.buttonRight.setOnClickListener(listener)
    }
}
