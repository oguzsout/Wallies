package com.oguzdogdu.wallieshd.presentation.profiledetail.userphotos

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.oguzdogdu.domain.model.userdetail.UsersPhotos
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.databinding.ItemUserCollectionPhotosBinding
import com.oguzdogdu.wallieshd.util.itemLoading
import com.oguzdogdu.wallieshd.util.loadImage

class UsersPhotosAdapter : ListAdapter<UsersPhotos, UsersPhotosAdapter.UsersPhotosListsViewHolder>(
    DIFF_CALLBACK
) {

    private var onItemClickListener: ((UsersPhotos?) -> Unit)? = null

    fun setOnItemClickListener(listener: (UsersPhotos?) -> Unit) {
        onItemClickListener = listener
    }

    inner class UsersPhotosListsViewHolder(private val binding: ItemUserCollectionPhotosBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(photo: UsersPhotos) {
            binding.apply {
                imageViewUserCollectionPhoto.loadImage(
                    photo.url,
                    placeholder = this.root.context.itemLoading(
                        R.color.purple_03
                    )
                )
                binding.root.setOnClickListener {
                    onItemClickListener?.let {
                        it(photo)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersPhotosListsViewHolder {
        return UsersPhotosListsViewHolder(
            ItemUserCollectionPhotosBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UsersPhotosListsViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<UsersPhotos> =
            object : DiffUtil.ItemCallback<UsersPhotos>() {
                override fun areItemsTheSame(
                    oldItem: UsersPhotos,
                    newItem: UsersPhotos
                ) =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: UsersPhotos,
                    newItem: UsersPhotos
                ) = oldItem == newItem
            }
    }
}
