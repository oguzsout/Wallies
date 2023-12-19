package com.oguzdogdu.wallieshd.presentation.profiledetail.usercollections

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.oguzdogdu.domain.model.userdetail.UserCollections
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.databinding.ItemCollectionsBinding
import com.oguzdogdu.wallieshd.util.itemLoading
import com.oguzdogdu.wallieshd.util.loadImage

class UserCollectionAdapter :
    ListAdapter<UserCollections, UserCollectionAdapter.CollectionsViewHolder>(
        DIFF_CALLBACK
    ) {

    private var onItemClickListener: ((UserCollections?) -> Unit)? = null

    fun setOnItemClickListener(listener: (UserCollections?) -> Unit) {
        onItemClickListener = listener
    }

    inner class CollectionsViewHolder(private val binding: ItemCollectionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(wallpaper: UserCollections) {
            binding.apply {
                textViewAuthorName.text = wallpaper.title
                imageViewPost.loadImage(
                    wallpaper.photo,
                    placeholder = this.root.context.itemLoading(
                        R.color.purple_03
                    )
                )
                imageViewPost.setOnClickListener {
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
        val DIFF_CALLBACK: DiffUtil.ItemCallback<UserCollections> =
            object : DiffUtil.ItemCallback<UserCollections>() {
                override fun areItemsTheSame(
                    oldItem: UserCollections,
                    newItem: UserCollections
                ) = oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: UserCollections,
                    newItem: UserCollections
                ) = oldItem == newItem
            }
    }
}
