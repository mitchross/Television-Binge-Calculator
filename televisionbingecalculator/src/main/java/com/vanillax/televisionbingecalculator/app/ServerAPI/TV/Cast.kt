package com.vanillax.televisionbingecalculator.app.ServerAPI.TV

import com.google.gson.annotations.SerializedName

/**
 * Created by mitchross on 5/21/17.
 */

class Cast {
    @SerializedName("name")
    var name: String? = null

    @SerializedName("profile_path")
    var profilePath: String? = null
}
