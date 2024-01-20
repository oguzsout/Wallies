package com.oguzdogdu.wallieshd.presentation.search.searchuser

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oguzdogdu.domain.model.search.searchuser.SearchUser
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BasePagingDataAdapter
import com.oguzdogdu.wallieshd.databinding.ItemUserSearchBinding
import com.oguzdogdu.wallieshd.util.itemLoading
import com.oguzdogdu.wallieshd.util.loadImage

class SearchUserAdapter : BasePagingDataAdapter<SearchUser, SearchUserAdapter.UserImageViewHolder>() {

    var onBindToDivider: ((binding: View, position: Int) -> Unit)? = null

    inner class UserImageViewHolder(private val binding: ItemUserSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ResourceAsColor")
        fun bind(user: SearchUser?) {
            binding.apply {
                imageViewUserProfileImage.loadImage(
                    user?.profileImage,
                    placeholder = this.root.context.itemLoading(
                        R.color.purple_03
                    ),
                    radius = true
                )
                textViewUsername.text = user?.name.orEmpty()

                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(user)
                    }
                }
                onBindToDivider?.invoke(binding.root, layoutPosition)
            }
        }
    }

    override fun createViewHolder(parent: ViewGroup): UserImageViewHolder {
        return UserImageViewHolder(
            ItemUserSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun bindViewHolder(holder: UserImageViewHolder, item: SearchUser) {
        holder.bind(item)
    }
}
