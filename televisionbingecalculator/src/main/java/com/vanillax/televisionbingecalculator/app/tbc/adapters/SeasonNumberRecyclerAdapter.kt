package com.vanillax.televisionbingecalculator.app.tbc.adapters

import androidx.databinding.ViewDataBinding
import com.vanillax.televisionbingecalculator.app.R


import com.vanillax.televisionbingecalculator.app.util.bindingadapter.BaseDataBindingAdapter
import com.vanillax.televisionbingecalculator.app.util.bindingadapter.DataBoundViewHolder
import com.vanillax.televisionbingecalculator.app.viewmodel.SeasonNumberViewModelItem

class SeasonNumberRecyclerAdapter( private val listener: SeasonNumberViewModelItem.SeasonNumberViewModelitemCallback) : BaseDataBindingAdapter<ViewDataBinding>() {

    internal var seasonList: ArrayList<SeasonNumberViewModelItem> = arrayListOf()

    fun setSeasonList(seasonNumberViewmodelList: ArrayList<SeasonNumberViewModelItem>) {
        seasonList.clear()
        for( s in seasonNumberViewmodelList)
        {
            seasonList.add(SeasonNumberViewModelItem(s.number,listener))
        }
        seasonList.addAll(seasonNumberViewmodelList)

        notifyDataSetChanged()
    }



    override fun bindItem(holder: DataBoundViewHolder<ViewDataBinding>?, position: Int, payloads: MutableList<Any>?) {
        if (holder != null) {
            holder.binding.setVariable(BR.viewModel, seasonList[position] )
            //todo why wont this compile?
            holder.binding.setVariable(BR.listener, listener)
        }
    }


    override fun getItemCount(): Int {
        return seasonList.size
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.season_number_item2
    }
}