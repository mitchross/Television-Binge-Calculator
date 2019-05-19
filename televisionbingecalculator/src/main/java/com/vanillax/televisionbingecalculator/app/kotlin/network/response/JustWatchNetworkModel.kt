package com.vanillax.televisionbingecalculator.app.kotlin.network.response

import com.google.gson.annotations.SerializedName
import com.vanillax.televisionbingecalculator.app.serverapi.movie.Scoring

class JustWatchSearch(
        @SerializedName("query")
        var justWatchSearchQuery: String
)

data class JustWatchSearchitem(

        @SerializedName("title")
        var title: String?,

        @SerializedName("offers")
        var offers: List<Offer>?,

        @SerializedName("original_release_year")
        var release_year: Int,

        @SerializedName("scoring")
        var scoringList: List<Scoring>?
)

data class JustWatchResponse(
        @SerializedName("items")
        var items: List<JustWatchSearchitem>?
)