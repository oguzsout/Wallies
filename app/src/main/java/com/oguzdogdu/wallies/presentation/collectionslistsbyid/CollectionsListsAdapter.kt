package com.oguzdogdu.wallies.presentation.collectionslistsbyid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.oguzdogdu.domain.model.collection.CollectionList
import com.oguzdogdu.wallies.databinding.ItemCollectionsListsBinding

class CollectionsListsAdapter :
    ListAdapter<CollectionList, CollectionsListsAdapter.CollectionsListsViewHolder>(
        DIFF_CALLBACK
    ) {

    private var onItemClickListener: ((CollectionList?) -> Unit)? = null

    fun setOnItemClickListener(listener: (CollectionList?) -> Unit) {
        onItemClickListener = listener
    }

    inner class CollectionsListsViewHolder(private val binding: ItemCollectionsListsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wallpaper: CollectionList) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionsListsViewHolder {
        return CollectionsListsViewHolder(
            ItemCollectionsListsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CollectionsListsViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<CollectionList> =
            object : DiffUtil.ItemCallback<CollectionList>() {
                override fun areItemsTheSame(
                    oldItem: CollectionList,
                    newItem: CollectionList
                ) =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: CollectionList,
                    newItem: CollectionList
                ) = oldItem == newItem
            }
    }
}
