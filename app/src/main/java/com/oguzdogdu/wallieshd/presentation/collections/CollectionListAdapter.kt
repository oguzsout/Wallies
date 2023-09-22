package com.oguzdogdu.wallieshd.presentation.collections

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.oguzdogdu.domain.model.collection.WallpaperCollections
import com.oguzdogdu.wallieshd.databinding.ItemCollectionsBinding

class CollectionListAdapter :
    PagingDataAdapter<WallpaperCollections, CollectionListAdapter.CollectionsViewHolder>(
        DIFF_CALLBACK
    ) {

    private var onItemClickListener: ((WallpaperCollections?) -> Unit)? = null

    fun setOnItemClickListener(listener: (WallpaperCollections?) -> Unit) {
        onItemClickListener = listener
    }

    inner class CollectionsViewHolder(private val binding: ItemCollectionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wallpaper: WallpaperCollections) {
            binding.apply {
                textViewAuthorName.text = wallpaper.title
                imageViewPost.load(wallpaper.photo)
                binding.root.setOnClickListener {
                    onItemClickListener?.let {
                        it(wallpaper)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionsViewHolder {
        return CollectionsViewHolder(
            ItemCollectionsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CollectionsViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<WallpaperCollections> =
            object : DiffUtil.ItemCallback<WallpaperCollections>() {
                override fun areItemsTheSame(
                    oldItem: WallpaperCollections,
                    newItem: WallpaperCollections
                ) =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: WallpaperCollections,
                    newItem: WallpaperCollections
                ) = oldItem == newItem
            }
    }
}
