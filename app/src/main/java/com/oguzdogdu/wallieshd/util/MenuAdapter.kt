package com.oguzdogdu.wallieshd.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.oguzdogdu.wallieshd.databinding.ItemMenuAdapterBinding
import com.oguzdogdu.wallieshd.presentation.collections.MenuAdapterItem
import com.skydoves.powermenu.MenuBaseAdapter

class MenuAdapter : MenuBaseAdapter<MenuAdapterItem>() {
    override fun getView(index: Int, view: View?, viewGroup: ViewGroup?): View {
        val binding = ItemMenuAdapterBinding.inflate(LayoutInflater.from(viewGroup?.context))
        val item = (getItem(index) as MenuAdapterItem)
        binding.textViewMenuTitle.text = item.title
        return super.getView(index, binding.root, viewGroup)
    }
}
