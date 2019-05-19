package com.vanillax.televisionbingecalculator.app.kotlin.network.response

import com.google.gson.annotations.SerializedName
import com.vanillax.televisionbingecalculator.app.serverapi.movie.Scoring

class JustWatchSearch(
        @SerializedName("query")
        val justWatchSearchQuery: String
)

data class JustWatchSearchitem(

        @SerializedName("title")
        val title: String? = null,

        @SerializedName("offers")
        val offers: List<Offer>? = listOf(),

        @SerializedName("original_release_year")
        val release_year: Int = 0,

        @SerializedName("scoring")
        val scoringList: List<Scoring>? = listOf()
)

data class JustWatchResponse(
        @SerializedName("items")
        val items: List<JustWatchSearchitem>? = listOf()
)