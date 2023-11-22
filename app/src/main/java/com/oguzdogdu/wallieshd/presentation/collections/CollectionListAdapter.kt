package com.oguzdogdu.wallieshd.presentation.collections

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.oguzdogdu.domain.model.collection.WallpaperCollections
import com.oguzdogdu.wallieshd.core.BasePagingDataAdapter
import com.oguzdogdu.wallieshd.databinding.ItemCollectionsBinding

class CollectionListAdapter :
    BasePagingDataAdapter<WallpaperCollections, CollectionListAdapter.CollectionsViewHolder>() {


    inner class CollectionsViewHolder(private val binding: ItemCollectionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wallpaper: WallpaperCollections) {
            binding.apply {
                textViewAuthorName.text = wallpaper.title
                imageViewPost.load(wallpaper.photo)
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(wallpaper)
                    }
                }
            }
        }
    }

    override fun createViewHolder(parent: ViewGroup): CollectionsViewHolder {
        return CollectionsViewHolder(
            ItemCollectionsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun bindViewHolder(holder: CollectionsViewHolder, item: WallpaperCollections) {
        holder.bind(item)
    }
}
