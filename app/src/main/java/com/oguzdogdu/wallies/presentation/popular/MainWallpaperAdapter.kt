package com.oguzdogdu.wallies.presentation.popular

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.oguzdogdu.data.model.UnsplashResponseItem
import com.oguzdogdu.domain.PopularImage
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.databinding.ItemMainImageBinding

class MainWallpaperAdapter :
    ListAdapter<PopularImage, MainWallpaperAdapter.MainImageViewHolder>(
        DIFF_CALLBACK
    ) {

    private var onItemClickListener: ((PopularImage?) -> Unit)? = null

    fun setOnItemClickListener(listener: (PopularImage?) -> Unit) {
        onItemClickListener = listener
    }

    inner class MainImageViewHolder(private val binding: ItemMainImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wallpaper: PopularImage?) {
            binding.apply {
                imageViewItemWallpaper.load(wallpaper?.url){
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
        val DIFF_CALLBACK: DiffUtil.ItemCallback<PopularImage> =
            object : DiffUtil.ItemCallback<PopularImage>() {
                override fun areItemsTheSame(
                    oldItem: PopularImage,
                    newItem: PopularImage,
                ) =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: PopularImage,
                    newItem: PopularImage,
                ) = oldItem == newItem
            }
    }
}