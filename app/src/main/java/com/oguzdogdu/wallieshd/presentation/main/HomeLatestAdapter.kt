package com.oguzdogdu.wallieshd.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oguzdogdu.domain.model.home.HomeListItems
import com.oguzdogdu.wallieshd.core.BaseListAdapter
import com.oguzdogdu.wallieshd.databinding.ItemHomePopularAndLatestBinding
import com.oguzdogdu.wallieshd.util.loadImage

class HomeLatestAdapter : BaseListAdapter<HomeListItems, HomeLatestAdapter.MainImageViewHolder>() {
    inner class MainImageViewHolder(private val binding: ItemHomePopularAndLatestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wallpaper: HomeListItems?) {
            binding.apply {
                imageViewItemWallpaper.loadImage(wallpaper?.url)
                imageViewItemWallpaper.setOnClickListener {
                    onItemClickListener?.invoke(wallpaper)
                }
            }
        }
    }

    override fun createViewHolder(parent: ViewGroup): MainImageViewHolder {
        return MainImageViewHolder(
            ItemHomePopularAndLatestBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun bindViewHolder(holder: MainImageViewHolder, item: HomeListItems) {
        holder.bind(item)
    }
}
