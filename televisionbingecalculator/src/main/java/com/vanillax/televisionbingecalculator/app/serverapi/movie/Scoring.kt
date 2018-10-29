package com.vanillax.televisionbingecalculator.app.serverapi.movie

import com.google.gson.annotations.SerializedName

/**
 * Created by mitchross on 5/20/17.
 */

class Scoring {
    @SerializedName("provider_type")
    var providerType: String? = null

    @SerializedName("value")
    var value: Double? = null
}
