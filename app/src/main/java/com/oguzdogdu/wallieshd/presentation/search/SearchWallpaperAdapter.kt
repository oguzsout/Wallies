package com.oguzdogdu.wallieshd.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oguzdogdu.domain.model.search.SearchPhoto
import com.oguzdogdu.wallieshd.core.BasePagingDataAdapter
import com.oguzdogdu.wallieshd.databinding.ItemMainImageBinding
import com.oguzdogdu.wallieshd.util.loadImage

class SearchWallpaperAdapter :
    BasePagingDataAdapter<SearchPhoto, SearchWallpaperAdapter.MainImageViewHolder>() {

    inner class MainImageViewHolder(private val binding: ItemMainImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wallpaper: SearchPhoto?) {
            binding.apply {
                imageViewItemWallpaper.loadImage(wallpaper?.url)
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

    override fun bindViewHolder(holder: MainImageViewHolder, item: SearchPhoto) {
        holder.bind(item)
    }
}
