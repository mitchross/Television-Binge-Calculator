package com.vanillax.televisionbingecalculator.app.viewmodel

import android.view.View
import android.widget.TextView
import com.vanillax.televisionbingecalculator.app.R
import com.vanillax.televisionbingecalculator.app.kotlin.viewmodels.DetailsViewModel

class SeasonNumberViewModelItem(internal var number: Int, detailsViewModelInterface: DetailsViewModel.DetailsViewModelInterface) {

    val seasonNumber: String
        get() = number.toString()

    val listener = detailsViewModelInterface

    fun onTouch(view: View)
    {


        listener.onSeasonNumberTouch(number)

    }

    fun se (): Int {
        return R.color.material_blue
    }

//    var blue  =view.itemView.resources.getColor(com.vanillax.televisionbingecalculator.app.R.color.material_blue)
//    holder.binding.seasonNumber.setOnClickListener { v-> Log.d("test", "im tappped")  }
//    holder.binding.seasonNumber.setTextColor(blue)

}
