package com.vanillax.televisionbingecalculator.app.tbc.adapters

import android.content.Context
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.vanillax.televisionbingecalculator.app.R
import com.vanillax.televisionbingecalculator.app.databinding.SeasonNumberItem2Binding
import com.vanillax.televisionbingecalculator.app.kotlin.viewmodels.DetailsViewModel
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

//        if ( holder !=null ) {
//
//             holder.binding.viewModel = seasonList[position]
//
//
//            //click listener
//            holder.binding.seasonNumber.setOnClickListener { v ->
//
//                Log.d("test", "it worked!" + position)
//
//                unselectAllSeasonsAndReapply(position, v as TextView)
//                //change color of item on tap
//               // setColor(position, v as TextView)
//                //update view to do new time calcuation based on season
//                listener.onSeasonNumberTouch(position)
//            }
//
//            var context: Context = holder.binding.seasonNumber.context
//
//            if(seasonList[position].isClicked )
//            {
//                var blue = ContextCompat.getColor(context, R.color.material_blue)
//                holder.binding.seasonNumber.setTextColor(blue)
//            }
//            else{
//                var white = ContextCompat.getColor(context, R.color.material_gray_200)
//                holder.binding.seasonNumber.setTextColor(white)
//            }
//
//
//        }
//

    }

    override fun getItemCount(): Int {
        return seasonList.size
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.season_number_item2
    }
}