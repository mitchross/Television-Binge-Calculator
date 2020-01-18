package com.vanillax.televisionbingecalculator.app.kotlin.network.response

import com.google.gson.annotations.SerializedName

/**
 * Created by mitchross on 5/5/17.
 */

data class Offer(@SerializedName("provider_id")
                 var providerId: Streamtype?) {

    enum class Streamtype(val value: Int) {
        @SerializedName("7")
        VUDU(7),
        @SerializedName("8")
        NETFLIX(8),
        @SerializedName("10")
        AMAZON(10),
        @SerializedName("31")
        HBO(31),
        @SerializedName("15")
        HULU(15)
    }
}
