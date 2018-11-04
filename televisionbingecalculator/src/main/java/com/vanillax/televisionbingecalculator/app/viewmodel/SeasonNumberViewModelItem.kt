package com.vanillax.televisionbingecalculator.app.viewmodel

import com.vanillax.televisionbingecalculator.app.kotlin.viewmodels.DetailsViewModel

class SeasonNumberViewModelItem(internal var number: Int, detailsViewModelInterface: DetailsViewModel.DetailsViewModelInterface) {

    val seasonNumber: String
        get() = number.toString()

    val listener = detailsViewModelInterface

    fun onTouch()
    {
         listener.onSeasonNumberTouch(number)

    }

}
