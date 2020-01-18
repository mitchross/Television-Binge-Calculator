package com.vanillax.televisionbingecalculator.app.viewmodel

import com.vanillax.televisionbingecalculator.app.R
import com.vanillax.televisionbingecalculator.app.kotlin.items.BaseItem
import com.vanillax.televisionbingecalculator.app.kotlin.viewmodels.DetailsViewModel2

class SeasonNumberViewModelItem(private val number: Int,
                                selected: Boolean,
                                private val callback: (action: DetailsViewModel2.DetailsAction) -> Unit) : BaseItem(R.layout.season_number_item2) {

    val seasonNumber: String
        get() = if (number == 0) "All" else number.toString()

    var textColor: Int = if (selected) R.color.material_blue else R.color.material_gray_200

    fun onItemClicked() {
        callback.invoke(DetailsViewModel2.DetailsAction.SeasonNumberClicked(number))
    }
}