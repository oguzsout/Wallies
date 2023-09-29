package com.oguzdogdu.wallieshd.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.oguzdogdu.wallieshd.databinding.ItemSuggestSearchBinding

class SuggestSearchWordsAdapter :
    ListAdapter<SuggestWords, SuggestSearchWordsAdapter.SuggestKeywordListsViewHolder>(
        DIFF_CALLBACK
    ) {
    private var onItemClickListener: ((SuggestWords?) -> Unit)? = null

    fun setOnItemClickListener(listener: (SuggestWords?) -> Unit) {
        onItemClickListener = listener
    }

    inner class SuggestKeywordListsViewHolder(private val binding: ItemSuggestSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(keyword: SuggestWords) {
            binding.chipItemSearchKeyword.text = keyword.keyword
            binding.chipItemSearchKeyword.setOnClickListener {
                onItemClickListener?.let {
                    it(keyword)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SuggestKeywordListsViewHolder {
        return SuggestKeywordListsViewHolder(
            ItemSuggestSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SuggestKeywordListsViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<SuggestWords> =
            object : DiffUtil.ItemCallback<SuggestWords>() {
                override fun areItemsTheSame(
                    oldItem: SuggestWords,
                    newItem: SuggestWords
                ) =
                    oldItem == newItem

                override fun areContentsTheSame(
                    oldItem: SuggestWords,
                    newItem: SuggestWords
                ) = oldItem == newItem
            }
    }
}
