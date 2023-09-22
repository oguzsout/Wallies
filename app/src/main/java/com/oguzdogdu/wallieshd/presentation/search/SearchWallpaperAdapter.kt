package com.oguzdogdu.wallieshd.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.oguzdogdu.domain.model.search.SearchPhoto
import com.oguzdogdu.wallieshd.databinding.ItemMainImageBinding

class SearchWallpaperAdapter :
    PagingDataAdapter<SearchPhoto, SearchWallpaperAdapter.MainImageViewHolder>(
        DIFF_CALLBACK
    ) {

    private var onItemClickListener: ((SearchPhoto?) -> Unit)? = null

    fun setOnItemClickListener(listener: (SearchPhoto?) -> Unit) {
        onItemClickListener = listener
    }

    inner class MainImageViewHolder(private val binding: ItemMainImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wallpaper: SearchPhoto?) {
            binding.apply {
                imageViewItemWallpaper.load(wallpaper?.url) {
                    diskCachePolicy(CachePolicy.DISABLED)
                }
                binding.root.setOnClickListener {
                    onItemClickListener?.let {
                        it(wallpaper)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainImageViewHolder {
        return MainImageViewHolder(
            ItemMainImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainImageViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<SearchPhoto> =
            object : DiffUtil.ItemCallback<SearchPhoto>() {
                override fun areItemsTheSame(
                    oldItem: SearchPhoto,
                    newItem: SearchPhoto
                ) =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: SearchPhoto,
                    newItem: SearchPhoto
                ) = oldItem == newItem
            }
    }
}
