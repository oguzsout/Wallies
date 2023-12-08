package com.oguzdogdu.wallieshd.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.oguzdogdu.domain.model.home.HomeListItems
import com.oguzdogdu.wallieshd.core.BaseListAdapter
import com.oguzdogdu.wallieshd.databinding.ItemHomePopularAndLatestBinding

class HomePopularAdapter : BaseListAdapter<HomeListItems, HomePopularAdapter.MainImageViewHolder>() {
    inner class MainImageViewHolder(private val binding: ItemHomePopularAndLatestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wallpaper: HomeListItems?) {
            binding.apply {
                imageViewItemWallpaper.load(wallpaper?.url) {
                    diskCachePolicy(CachePolicy.DISABLED)
                }
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
