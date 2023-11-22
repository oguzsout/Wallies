package com.oguzdogdu.wallieshd.presentation.latest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.oguzdogdu.domain.model.latest.LatestImage
import com.oguzdogdu.wallieshd.core.BasePagingDataAdapter
import com.oguzdogdu.wallieshd.databinding.ItemMainImageBinding

class LatestWallpaperAdapter :
    BasePagingDataAdapter<LatestImage, LatestWallpaperAdapter.MainImageViewHolder>() {

    inner class MainImageViewHolder(private val binding: ItemMainImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wallpaper: LatestImage?) {
            binding.apply {
                imageViewItemWallpaper.load(wallpaper?.url) {
                    diskCachePolicy(CachePolicy.DISABLED)
                }
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(wallpaper)
                    }
                }
            }
        }
    }

    override fun createViewHolder(parent: ViewGroup): MainImageViewHolder {
        return MainImageViewHolder(
            ItemMainImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun bindViewHolder(holder: MainImageViewHolder, item: LatestImage) {
        holder.bind(item)
    }
}
