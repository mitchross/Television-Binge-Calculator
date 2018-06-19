package com.vanillax.televisionbingecalculator.app.ServerAPI.TV

import com.google.gson.annotations.SerializedName
import com.vanillax.televisionbingecalculator.app.Kotlin.network.response.Cast

/**
 * Created by mitchross on 5/21/17.
 */

class CastResponse {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("cast")
    var castList: List<Cast>? = null
}
