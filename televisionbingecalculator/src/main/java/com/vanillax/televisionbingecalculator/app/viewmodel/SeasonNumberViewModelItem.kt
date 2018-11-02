package com.vanillax.televisionbingecalculator.app.viewmodel

import com.vanillax.televisionbingecalculator.app.kotlin.viewmodels.DetailsViewModel

class SeasonNumberViewModelItem(internal var number: String, detailsViewModelInterface: DetailsViewModel.DetailsViewModelInterface) {

    val seasonNumber: String?
        get() = number

    val listener = detailsViewModelInterface

    fun onTouch()
    {
        seasonNumber?.toInt()?.let { listener.onSeasonNumberTouch(it) }

    }

}
