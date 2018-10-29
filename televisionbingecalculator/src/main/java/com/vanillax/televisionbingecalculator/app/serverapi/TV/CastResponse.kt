package com.vanillax.televisionbingecalculator.app.serverapi.TV

import com.google.gson.annotations.SerializedName
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.Cast

/**
 * Created by mitchross on 5/21/17.
 */

class CastResponse {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("cast")
    var castList: List<Cast>? = null
}
