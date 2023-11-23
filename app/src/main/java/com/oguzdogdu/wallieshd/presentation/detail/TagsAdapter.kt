package com.oguzdogdu.wallieshd.presentation.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.oguzdogdu.wallieshd.databinding.ItemSuggestSearchBinding

class TagsAdapter : ListAdapter<String, TagsAdapter.TagsListsViewHolder>(
    DIFF_CALLBACK
) {
    private var onItemClickListener: ((String?) -> Unit)? = null

    fun setOnItemClickListener(listener: (String?) -> Unit) {
        onItemClickListener = listener
    }

    inner class TagsListsViewHolder(private val binding: ItemSuggestSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tag: String?) {
            binding.chipItemSearchKeyword.text = tag
            binding.chipItemSearchKeyword.setOnClickListener {
                onItemClickListener?.let {
                    it(tag)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsListsViewHolder {
        return TagsListsViewHolder(
            ItemSuggestSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TagsListsViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<String> =
            object : DiffUtil.ItemCallback<String>() {
                override fun areItemsTheSame(
                    oldItem: String,
                    newItem: String
                ) = oldItem == newItem

                override fun areContentsTheSame(
                    oldItem: String,
                    newItem: String
                ) = oldItem == newItem
            }
    }
}
