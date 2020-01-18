package com.vanillax.televisionbingecalculator.app.viewmodel

import com.vanillax.televisionbingecalculator.app.R
import com.vanillax.televisionbingecalculator.app.kotlin.items.BaseItem
import com.vanillax.televisionbingecalculator.app.kotlin.utils.CalculatorUtils
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.Cast

/**
 * Created by mitchross on 5/21/17.
 */

class CastViewModelItem(private val cast: Cast): BaseItem(R.layout.cast_listing) {

    val castImage: String
        get() = CalculatorUtils.getShowPosterThumbnail(cast.profilePath, false)

    val castName: String?
        get() = cast.name

    val characterName: String?
        get() = cast.character
}
