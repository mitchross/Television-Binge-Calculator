package com.vanillax.televisionbingecalculator.app.tbc.adapters

import com.vanillax.televisionbingecalculator.app.R
import com.vanillax.televisionbingecalculator.app.databinding.SeasonNumberItem2Binding
import com.vanillax.televisionbingecalculator.app.kotlin.viewmodels.DetailsViewModel
import com.vanillax.televisionbingecalculator.app.util.bindingadapter.BaseDataBindingAdapter
import com.vanillax.televisionbingecalculator.app.util.bindingadapter.DataBoundViewHolder
import com.vanillax.televisionbingecalculator.app.viewmodel.SeasonNumberViewModelItem


class SeasonNumberRecyclerAdapter (private var listener: DetailsViewModel.DetailsViewModelInterface) : BaseDataBindingAdapter<com.vanillax.televisionbingecalculator.app.databinding.SeasonNumberItem2Binding>()
{
    internal var seasonList: MutableList<SeasonNumberViewModelItem> = ArrayList()

    fun setListener( listener: DetailsViewModel.DetailsViewModelInterface)
    {
        this.listener = listener
    }

    fun setSeasonList(seasonsCount: Int) {
        seasonList.clear()


        for (i in 1 until seasonsCount + 1) {

            seasonList.add(SeasonNumberViewModelItem( i, listener ))
        }

        notifyDataSetChanged()
    }

    override fun bindItem(holder: DataBoundViewHolder<SeasonNumberItem2Binding>, position: Int, payloads: List<Any>) {

        if ( holder !=null ) {
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