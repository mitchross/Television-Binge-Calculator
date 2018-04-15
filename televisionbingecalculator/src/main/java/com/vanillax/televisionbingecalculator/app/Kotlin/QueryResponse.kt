package com.vanillax.televisionbingecalculator.app.Kotlin

import com.google.gson.annotations.SerializedName

/**
 * Created by mitchross on 4/14/18.
 */

class QueryResponse(@SerializedName("results")
                    val showPosterListing: List<com.vanillax.televisionbingecalculator.app.Kotlin.ShowPosterListing>)

data class ShowPosterListing(
        val id: Int,
        @SerializedName( "poster_path" ) val posterPath: String,
        @SerializedName( "original_name" ) val original_name: String,
        @SerializedName("original_title") val movie_title: String
)

