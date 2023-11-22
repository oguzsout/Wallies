package com.oguzdogdu.wallieshd.presentation.popular

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.oguzdogdu.domain.model.popular.PopularImage
import com.oguzdogdu.wallieshd.core.BasePagingDataAdapter
import com.oguzdogdu.wallieshd.databinding.ItemMainImageBinding

class PopularWallpaperAdapter :
    BasePagingDataAdapter<PopularImage, PopularWallpaperAdapter.MainImageViewHolder>() {

    inner class MainImageViewHolder(private val binding: ItemMainImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wallpaper: PopularImage?) {
            binding.apply {
                imageViewItemWallpaper.load(wallpaper?.url) {
                    diskCachePolicy(CachePolicy.DISABLED)
                }
                imageViewItemWallpaper.setOnClickListener {
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

    override fun bindViewHolder(holder: MainImageViewHolder, item: PopularImage) {
        holder.bind(item)
    }
}
