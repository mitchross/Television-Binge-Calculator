package com.vanillax.televisionbingecalculator.app.serverapi.movie

import com.google.gson.annotations.SerializedName

/**
 * Created by mitchross on 5/20/17.
 */

data class Scoring(
        @SerializedName("provider_type")
        val providerType: String? = null,

        @SerializedName("value")
        val value: Double? = null
)
