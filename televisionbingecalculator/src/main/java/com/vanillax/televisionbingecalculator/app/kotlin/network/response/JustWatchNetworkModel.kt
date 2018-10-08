package com.vanillax.televisionbingecalculator.app.kotlin.network.response

import com.google.gson.annotations.SerializedName
import com.vanillax.televisionbingecalculator.app.ServerAPI.movie.Scoring

class JustWatchSearch(@SerializedName("query")
                         var justWatchSearchQuery: String )

class JustWatchSearchitem (

        @SerializedName("title")
        var title: String? = null,

        @SerializedName("offers")
        var offers: List<Offer>?,

        @SerializedName("original_release_year")
        var release_year: Int = 0,

        @SerializedName("scoring")
        var scoringList: List<Scoring>? = null
)

class JustWatchResponse(@SerializedName("items")
                         var items: List<JustWatchSearchitem>? = null)