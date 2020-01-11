package com.vanillax.televisionbingecalculator.app.tbc.adapters

import androidx.databinding.ViewDataBinding
import com.vanillax.televisionbingecalculator.app.BR
import com.vanillax.televisionbingecalculator.app.R
import com.vanillax.televisionbingecalculator.app.util.bindingadapter.BaseDataBindingAdapter
import com.vanillax.televisionbingecalculator.app.util.bindingadapter.DataBoundViewHolder
import com.vanillax.televisionbingecalculator.app.viewmodel.SeasonNumberViewModelItem


class SeasonNumberRecyclerAdapter( private val callback: ( number: Int ) -> Unit) : BaseDataBindingAdapter<ViewDataBinding>() {

    internal var seasonList: ArrayList<SeasonNumberViewModelItem> = arrayListOf()

    fun setSeasonList(seasonNumberViewmodelList: ArrayList<SeasonNumberViewModelItem>) {

        if (this.seasonList == seasonNumberViewmodelList) {
            return
        }

        seasonList.clear()
        for( s in seasonNumberViewmodelList)
        {
            seasonList.add(SeasonNumberViewModelItem(s.number,callback))
        }
        seasonList.addAll(seasonNumberViewmodelList)

        notifyDataSetChanged()

        this.seasonList = seasonNumberViewmodelList
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