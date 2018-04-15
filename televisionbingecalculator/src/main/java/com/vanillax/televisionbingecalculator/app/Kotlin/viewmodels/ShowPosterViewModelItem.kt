package com.vanillax.televisionbingecalculator.app.Kotlin.viewmodels

import com.vanillax.televisionbingecalculator.app.Kotlin.ShowPosterListing
import com.vanillax.televisionbingecalculator.app.TBC.Utils.CalculatorUtils

/**
 * Created by mitchross on 2/6/17.
 */

class ShowPosterViewModelItem( showPosterListing: ShowPosterListing, searchType: String) {

    val searchType = searchType

    val showPosterListing = showPosterListing

    val titlez: String
        get() = showPosterListing.original_name

    val posterUrl: String
        get() = CalculatorUtils.getShowPosterThumbnail(showPosterListing.posterPath, false)

    val id: Int
        get() = showPosterListing.id


}
