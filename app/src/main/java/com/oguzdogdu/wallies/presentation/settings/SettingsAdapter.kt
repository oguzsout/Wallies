package com.oguzdogdu.wallies.presentation.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.oguzdogdu.wallies.databinding.ItemOptionalMenuBinding
import com.oguzdogdu.wallies.presentation.authenticateduser.ProfileMenu

class SettingsAdapter : ListAdapter<ProfileMenu, SettingsAdapter.SettingsOptionsViewHolder>(
    DIFF_CALLBACK
) {

    private var onItemClickListener: ((ProfileMenu?) -> Unit)? = null

    fun setOnItemClickListener(listener: (ProfileMenu?) -> Unit) {
        onItemClickListener = listener
    }

    var onBindToDivider: ((binding: View, position: Int) -> Unit)? = null

    inner class SettingsOptionsViewHolder(private val binding: ItemOptionalMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(profile: ProfileMenu) {
            binding.apply {
                imageViewOptionalIcon.load(profile.iconRes)
                profile.titleRes?.let { textViewOptionalDesc.setText(it) }
                binding.root.setOnClickListener {
                    onItemClickListener?.let {
                        it(profile)
                    }
                }
                onBindToDivider?.invoke(binding.root, layoutPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsOptionsViewHolder {
        return SettingsOptionsViewHolder(
            ItemOptionalMenuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SettingsOptionsViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ProfileMenu> =
            object : DiffUtil.ItemCallback<ProfileMenu>() {
                override fun areItemsTheSame(
                    oldItem: ProfileMenu,
                    newItem: ProfileMenu
                ) = oldItem == newItem

                override fun areContentsTheSame(
                    oldItem: ProfileMenu,
                    newItem: ProfileMenu
                ) = oldItem == newItem
            }
    }
}
