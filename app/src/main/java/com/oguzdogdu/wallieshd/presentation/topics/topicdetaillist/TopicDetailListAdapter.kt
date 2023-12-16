package com.oguzdogdu.wallieshd.presentation.topics.topicdetaillist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oguzdogdu.domain.model.topics.TopicDetail
import com.oguzdogdu.wallieshd.core.BasePagingDataAdapter
import com.oguzdogdu.wallieshd.databinding.ItemMainImageBinding
import com.oguzdogdu.wallieshd.util.loadImage

class TopicDetailListAdapter :
    BasePagingDataAdapter<TopicDetail, TopicDetailListAdapter.TopicListViewHolder>() {

    inner class TopicListViewHolder(private val binding: ItemMainImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(topic: TopicDetail?) {
            binding.apply {
                imageViewItemWallpaper.loadImage(topic?.url)
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
