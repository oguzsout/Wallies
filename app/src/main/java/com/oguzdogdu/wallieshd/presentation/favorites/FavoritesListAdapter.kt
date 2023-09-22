package com.oguzdogdu.wallieshd.presentation.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.oguzdogdu.domain.model.favorites.FavoriteImages
import com.oguzdogdu.wallieshd.databinding.ItemMainImageBinding

class FavoritesListAdapter : ListAdapter<FavoriteImages, FavoritesListAdapter.FavoritesViewHolder>(
    DIFF_CALLBACK
) {

    private var onItemClickListener: ((FavoriteImages?) -> Unit)? = null

    fun setOnItemClickListener(listener: (FavoriteImages?) -> Unit) {
        onItemClickListener = listener
    }

    inner class FavoritesViewHolder(private val binding: ItemMainImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wallpaper: FavoriteImages) {
            binding.apply {
                imageViewItemWallpaper.load(wallpaper.url)
                binding.root.setOnClickListener {
                    onItemClickListener?.let {
                        it(wallpaper)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        return FavoritesViewHolder(
            ItemMainImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<FavoriteImages> =
            object : DiffUtil.ItemCallback<FavoriteImages>() {
                override fun areItemsTheSame(
                    oldItem: FavoriteImages,
                    newItem: FavoriteImages
                ) =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: FavoriteImages,
                    newItem: FavoriteImages
                ) = oldItem == newItem
            }
    }
}
