package com.vanillax.televisionbingecalculator.app.tbc.adapters

import android.util.Log
import android.widget.TextView
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
            //click listener
            holder.binding.seasonNumber.setOnClickListener { v ->

                Log.d("test", "it worked!" + position)
                //change color of item on tap
                setColor(position, v as TextView)
                //update view to do new time calcuation based on season
                listener.onSeasonNumberTouch(position)
            }

        }


    }

    fun setColor( seasonNumber: Int, view: TextView )
    {
        //how do I tell adapter to uncolor other items in the recycler view to not be blue and
        //make only the selected item blue?
        //I know when a item is tapped, but I dont know how to tell adapter to uncolor an item because
        //coloring happens at click logic, I dont have a way back into bind item?


        var blue = view.resources.getColor(com.vanillax.televisionbingecalculator.app.R.color.material_blue)
        view.setTextColor(blue)
    }

    override fun getItemCount(): Int {
        return seasonList.size
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.season_number_item2
    }


}