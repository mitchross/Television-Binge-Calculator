package com.vanillax.televisionbingecalculator.app.viewmodel

import com.vanillax.televisionbingecalculator.app.kotlin.utils.CalculatorUtils
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.Cast

/**
 * Created by mitchross on 5/21/17.
 */

class CastViewModelItem(internal var cast: Cast) {

    val castImage: String
        get() = CalculatorUtils.getShowPosterThumbnail(cast.profilePath, false)


    val castName: String?
        get() = cast.name

    val characterName: String?
        get() = cast.character


}
