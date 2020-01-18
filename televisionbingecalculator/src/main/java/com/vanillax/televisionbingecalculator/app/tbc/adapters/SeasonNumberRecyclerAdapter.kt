package com.vanillax.televisionbingecalculator.app.tbc.adapters

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.vanillax.televisionbingecalculator.app.BR
import com.vanillax.televisionbingecalculator.app.R
import com.vanillax.televisionbingecalculator.app.util.bindingadapter.BaseDataBindingAdapter
import com.vanillax.televisionbingecalculator.app.util.bindingadapter.BaseDataBindingListAdapter
import com.vanillax.televisionbingecalculator.app.util.bindingadapter.DataBoundViewHolder
import com.vanillax.televisionbingecalculator.app.viewmodel.SeasonNumberViewModelItem


class SeasonNumberRecyclerAdapter : BaseDataBindingAdapter<ViewDataBinding>() {

    private var seasonList: MutableList<SeasonNumberViewModelItem> = mutableListOf()

    fun setSeasonList(seasonNumberViewmodelList: List<SeasonNumberViewModelItem>) {
        seasonList.clear()
        seasonList.addAll(seasonNumberViewmodelList)

        notifyDataSetChanged()
    }

    override fun bindItem(holder: DataBoundViewHolder<ViewDataBinding>?, position: Int, payloads: MutableList<Any>?) {
        if (holder != null) {
            holder.binding.setVariable(BR.viewModel, seasonList[position] )
        }
    }

    override fun getItemCount(): Int {
        return seasonList.size
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.season_number_item2
    }
}

class SeasonNumberRecyclerAdapter2 : BaseDataBindingListAdapter<SeasonNumberViewModelItem>(DiffUtilItemCallback()) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    class DiffUtilItemCallback: DiffUtil.ItemCallback<SeasonNumberViewModelItem>() {

        override fun areItemsTheSame(oldItem: SeasonNumberViewModelItem,
                                     newItem: SeasonNumberViewModelItem): Boolean {
            return oldItem.seasonNumber == newItem.seasonNumber
        }

        override fun areContentsTheSame(oldItem: SeasonNumberViewModelItem,
                                        newItem: SeasonNumberViewModelItem): Boolean {
            return oldItem.textColor == newItem.textColor
        }
    }
}