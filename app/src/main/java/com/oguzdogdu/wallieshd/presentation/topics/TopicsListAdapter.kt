package com.oguzdogdu.wallieshd.presentation.topics

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oguzdogdu.domain.model.topics.Topics
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BasePagingDataAdapter
import com.oguzdogdu.wallieshd.databinding.ItemCollectionsBinding
import com.oguzdogdu.wallieshd.util.itemLoading
import com.oguzdogdu.wallieshd.util.loadImage
import com.oguzdogdu.wallieshd.util.shadow

class TopicsListAdapter :
    BasePagingDataAdapter<Topics, TopicsListAdapter.TopicsListViewHolder>() {

    inner class TopicsListViewHolder(private val binding: ItemCollectionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(topics: Topics?) {
            binding.apply {
                imageViewPost.shadow(100)
                textViewAuthorName.text = topics?.title
                imageViewPost.loadImage(
                    topics?.titleBackground,
                    placeholder = this.root.context.itemLoading(
                        R.color.purple_03
                    )
                )
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(topics)
                    }
                }
            }
        }
    }

    override fun createViewHolder(parent: ViewGroup): TopicsListViewHolder {
        return TopicsListViewHolder(
            ItemCollectionsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun bindViewHolder(holder: TopicsListViewHolder, item: Topics) {
        holder.bind(item)
    }
}
