package com.vanillax.televisionbingecalculator.app.Kotlin.network.response

import com.google.gson.annotations.SerializedName

/**
 * Created by mitchross on 5/5/17.
 */

class Offer {


    @SerializedName("provider_id")
    var providerId: Int = 0

    enum class Streamtype (val value: Int ) {
         VUDU(7),
         NETFLIX (8),
         AMAZON (10),
         HBO (31),
         HULU  (15)
    }


}
