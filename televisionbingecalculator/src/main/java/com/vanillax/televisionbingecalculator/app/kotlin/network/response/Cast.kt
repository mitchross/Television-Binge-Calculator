package com.vanillax.televisionbingecalculator.app.kotlin.network.response

import com.google.gson.annotations.SerializedName

/**
 * Created by mitchross on 5/21/17.
 */

data class Cast (
    @SerializedName("name")
    val name: String? = null,

    @SerializedName("profile_path")
    val profilePath: String? = null,

    @SerializedName("character")
    val character: String? = null
)
