package com.oguzdogdu.wallieshd.presentation.latest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.oguzdogdu.domain.model.latest.LatestImage
import com.oguzdogdu.wallieshd.databinding.ItemMainImageBinding

class LatestWallpaperAdapter :
    PagingDataAdapter<LatestImage, LatestWallpaperAdapter.MainImageViewHolder>(
        DIFF_CALLBACK
    ) {

    private var onItemClickListener: ((LatestImage?) -> Unit)? = null

    fun setOnItemClickListener(listener: (LatestImage?) -> Unit) {
        onItemClickListener = listener
    }

    inner class MainImageViewHolder(private val binding: ItemMainImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wallpaper: LatestImage?) {
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
        val DIFF_CALLBACK: DiffUtil.ItemCallback<LatestImage> =
            object : DiffUtil.ItemCallback<LatestImage>() {
                override fun areItemsTheSame(
                    oldItem: LatestImage,
                    newItem: LatestImage
                ) =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: LatestImage,
                    newItem: LatestImage
                ) = oldItem == newItem
            }
    }
}
