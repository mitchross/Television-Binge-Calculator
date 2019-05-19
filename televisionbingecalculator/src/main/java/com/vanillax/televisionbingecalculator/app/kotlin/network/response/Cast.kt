package com.vanillax.televisionbingecalculator.app.kotlin.network.response

import com.google.gson.annotations.SerializedName

/**
 * Created by mitchross on 5/21/17.
 */

data class Cast (
    @SerializedName("name")
    var name: String? = null,

    @SerializedName("profile_path")
    var profilePath: String? = null,

    @SerializedName("character")
    var character: String? = null
)
