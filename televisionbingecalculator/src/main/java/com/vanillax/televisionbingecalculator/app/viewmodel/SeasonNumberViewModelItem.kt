package com.vanillax.televisionbingecalculator.app.viewmodel

import androidx.databinding.ObservableInt
import com.vanillax.televisionbingecalculator.app.R

class SeasonNumberViewModelItem(internal var number: Int,
                                seasonClickListener: SeasonNumberViewModelitemCallback) {

    interface SeasonNumberViewModelitemCallback {
        fun onSeasonTouch(seasonNumber: Int)
    }

    val listener = seasonClickListener

    val seasonNumber: String
        get() = if (number == 0) "All" else number.toString()

    var textColor: ObservableInt = ObservableInt(R.color.material_gray_200)

    init {
        if (number == 0) {
            textColor.set(R.color.material_blue)
        }
    }

    fun setColorToDefault() {
        textColor.set(R.color.material_gray_200)
    }

    fun onItemClicked() {
        listener.onSeasonTouch(number)
        textColor.set(R.color.material_blue)
    }
}
