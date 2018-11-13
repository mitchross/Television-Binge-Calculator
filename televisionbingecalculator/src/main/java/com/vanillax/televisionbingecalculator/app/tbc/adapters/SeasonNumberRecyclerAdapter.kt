package com.vanillax.televisionbingecalculator.app.tbc.adapters

import com.vanillax.televisionbingecalculator.app.R
import com.vanillax.televisionbingecalculator.app.databinding.SeasonNumberItem2Binding
import com.vanillax.televisionbingecalculator.app.util.bindingadapter.BaseDataBindingAdapter
import com.vanillax.televisionbingecalculator.app.util.bindingadapter.DataBoundViewHolder
import com.vanillax.televisionbingecalculator.app.viewmodel.SeasonNumberViewModelItem

class SeasonNumberRecyclerAdapter : BaseDataBindingAdapter<com.vanillax.televisionbingecalculator.app.databinding.SeasonNumberItem2Binding>() {

    internal var seasonList: ArrayList<SeasonNumberViewModelItem> = arrayListOf()

    fun setSeasonList(seasonNumberViewmodelList: ArrayList<SeasonNumberViewModelItem>) {
        seasonList.clear()
        seasonList.addAll(seasonNumberViewmodelList)

        notifyDataSetChanged()
    }

    override fun bindItem(holder: DataBoundViewHolder<SeasonNumberItem2Binding>, position: Int, payloads: List<Any>) {

        if (holder != null) {
            holder.binding.viewModel = seasonList[position]
        }
    }

    override fun getItemCount(): Int {
        return seasonList.size
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.season_number_item2
    }
}