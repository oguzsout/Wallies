package com.oguzdogdu.wallieshd.presentation.latest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oguzdogdu.domain.model.latest.LatestImage
import com.oguzdogdu.wallieshd.core.BasePagingDataAdapter
import com.oguzdogdu.wallieshd.databinding.ItemMainImageBinding
import com.oguzdogdu.wallieshd.util.loadImage

class LatestWallpaperAdapter :
    BasePagingDataAdapter<LatestImage, LatestWallpaperAdapter.MainImageViewHolder>() {

    inner class MainImageViewHolder(private val binding: ItemMainImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wallpaper: LatestImage?) {
            binding.apply {
                imageViewItemWallpaper.loadImage(url = wallpaper?.url)
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
