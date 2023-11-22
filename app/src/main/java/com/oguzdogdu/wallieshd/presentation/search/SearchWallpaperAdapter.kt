package com.oguzdogdu.wallieshd.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.oguzdogdu.domain.model.search.SearchPhoto
import com.oguzdogdu.wallieshd.core.BasePagingDataAdapter
import com.oguzdogdu.wallieshd.databinding.ItemMainImageBinding

class SearchWallpaperAdapter :
    BasePagingDataAdapter<SearchPhoto, SearchWallpaperAdapter.MainImageViewHolder>() {

    inner class MainImageViewHolder(private val binding: ItemMainImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wallpaper: SearchPhoto?) {
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

    override fun bindViewHolder(holder: MainImageViewHolder, item: SearchPhoto) {
        holder.bind(item)
    }
}
