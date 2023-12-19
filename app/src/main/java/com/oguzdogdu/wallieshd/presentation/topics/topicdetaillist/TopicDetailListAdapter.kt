package com.oguzdogdu.wallieshd.presentation.topics.topicdetaillist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oguzdogdu.domain.model.topics.TopicDetail
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BasePagingDataAdapter
import com.oguzdogdu.wallieshd.databinding.ItemMainImageBinding
import com.oguzdogdu.wallieshd.util.itemLoading
import com.oguzdogdu.wallieshd.util.loadImage

class TopicDetailListAdapter :
    BasePagingDataAdapter<TopicDetail, TopicDetailListAdapter.TopicListViewHolder>() {

    inner class TopicListViewHolder(private val binding: ItemMainImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(topic: TopicDetail?) {
            binding.apply {
                imageViewItemWallpaper.loadImage(
                    topic?.url,
                    placeholder = this.root.context.itemLoading(
                        R.color.purple_03
                    )
                )
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(topic)
                    }
                }
            }
        }
    }

    override fun createViewHolder(parent: ViewGroup): TopicListViewHolder {
        return TopicListViewHolder(
            ItemMainImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun bindViewHolder(holder: TopicListViewHolder, item: TopicDetail) {
        holder.bind(item)
    }
}
