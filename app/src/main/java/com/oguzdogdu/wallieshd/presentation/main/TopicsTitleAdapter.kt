package com.oguzdogdu.wallieshd.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.oguzdogdu.domain.model.topics.Topics
import com.oguzdogdu.wallieshd.databinding.ItemTopicsTitleBinding
import com.oguzdogdu.wallieshd.util.loadImage
import com.oguzdogdu.wallieshd.util.shadow

class TopicsTitleAdapter : ListAdapter<Topics, TopicsTitleAdapter.TopicsTitleViewHolder>(
    DIFF_CALLBACK
) {
    private var onItemClickListener: ((Topics?) -> Unit)? = null

    fun setOnItemClickListener(listener: (Topics?) -> Unit) {
        onItemClickListener = listener
    }

    inner class TopicsTitleViewHolder(private val binding: ItemTopicsTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(topic: Topics) {
            binding.apply {
                imageViewTopicTitleBackground.shadow(150)
                imageViewTopicTitleBackground.loadImage(topic.titleBackground)
                textViewTopicTitle.text = topic.title
                binding.root.setOnClickListener {
                    onItemClickListener?.let {
                        it(topic)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicsTitleViewHolder {
        return TopicsTitleViewHolder(
            ItemTopicsTitleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TopicsTitleViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Topics> =
            object : DiffUtil.ItemCallback<Topics>() {
                override fun areItemsTheSame(
                    oldItem: Topics,
                    newItem: Topics
                ) =
                    oldItem == newItem

                override fun areContentsTheSame(
                    oldItem: Topics,
                    newItem: Topics
                ) = oldItem == newItem
            }
    }
}
