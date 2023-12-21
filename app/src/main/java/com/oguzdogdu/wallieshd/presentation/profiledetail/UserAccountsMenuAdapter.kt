package com.oguzdogdu.wallieshd.presentation.profiledetail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.oguzdogdu.wallieshd.databinding.ItemMenuAdapterBinding
import com.skydoves.powermenu.MenuBaseAdapter

class UserAccountsMenuAdapter : MenuBaseAdapter<UserSocialAccountsMenu>() {
    @SuppressLint("ViewHolder")
    override fun getView(index: Int, view: View?, viewGroup: ViewGroup?): View {
        val binding = ItemMenuAdapterBinding.inflate(LayoutInflater.from(viewGroup?.context))
        val item = (getItem(index) as UserSocialAccountsMenu)
        binding.textViewMenuTitle.text = item.title
        binding.imageViewTitleIcon.setImageDrawable(item.titleIcon)
        return super.getView(index, binding.root, viewGroup)
    }
}
