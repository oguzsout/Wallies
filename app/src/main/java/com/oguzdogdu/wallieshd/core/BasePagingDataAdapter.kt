package com.oguzdogdu.wallieshd.core

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BasePagingDataAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    private val diffCallback: DiffUtil.ItemCallback<T> = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T) =
            oldItem == newItem

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T) =
            oldItem == newItem
    }
) : PagingDataAdapter<T, VH>(diffCallback) {

    @JvmField
    var onItemClickListener: ((T?) -> Unit)? = null

    fun setOnItemClickListener(listener: (T?) -> Unit) {
        onItemClickListener = listener
    }

    protected abstract fun createViewHolder(parent: ViewGroup): VH

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        getItem(position)?.let { item ->
            bindViewHolder(holder, item)
        }
    }

    protected abstract fun bindViewHolder(holder: VH, item: T)
}
