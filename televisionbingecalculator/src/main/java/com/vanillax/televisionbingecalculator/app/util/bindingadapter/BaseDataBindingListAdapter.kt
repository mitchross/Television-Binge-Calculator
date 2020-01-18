package com.vanillax.televisionbingecalculator.app.util.bindingadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.LayoutInflaterCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vanillax.televisionbingecalculator.app.BR

abstract class BaseDataBindingListAdapter<T>(diffUtilItemCallback: DiffUtil.ItemCallback<T>) : ListAdapter<T, GenericViewHolder<T>>(diffUtilItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): GenericViewHolder<T> {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return GenericViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenericViewHolder<T>,
                                  position: Int) {
        holder.bind(getItem(position))
    }
}

class GenericViewHolder<T>(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: T) {
        binding.setVariable(BR.viewModel, item)
        binding.executePendingBindings()
    }
}