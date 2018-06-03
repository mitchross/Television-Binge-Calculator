package com.vanillax.televisionbingecalculator.app.Kotlin.network.response

import com.google.gson.annotations.SerializedName
import com.vanillax.televisionbingecalculator.app.ServerAPI.TV.Cast

/**
 * Created by mitchross on 4/14/18.
 */

class QueryResponse(@SerializedName("results")
                    val showPosterListing: List<ShowPosterListing>)

data class ShowPosterListing(
        val id: Int,
        @SerializedName( "poster_path" ) val posterPath: String,
        @SerializedName( "original_name" ) val original_name: String,
        @SerializedName("original_title") val movie_title: String
)

 class TVShowByIdResponse (
        @SerializedName("backdrop_path")
        var imageUrl: String?,

        @SerializedName("episode_run_time")
        var episodeRunTimeArray: IntArray ,

        @SerializedName("original_name")
        var title: String? = null,

        @SerializedName("original_title")
        var movie_title: String? = null,

        @SerializedName("overview")
        var episodeDescription: String? = null,

        @SerializedName("number_of_seasons")
        var numberOfSeasons: Int = 0,

        @SerializedName("number_of_episodes")
        var numberOfEpisodes: Int = 0,

        @SerializedName("first_air_date")
        var firstAirDate: String? = null,

        @SerializedName("genres")
        var genres: List<GenreClass>? = null,

        //movie
        //terrible idea fix later in another object
        @SerializedName("runtime")
        var movie_runtime: Int = 0,

        @SerializedName("release_date")
        var movie_release_date: String? = null,

        var seasons: List<Seasons>
)

data class GenreClass (val name: String )

data class Seasons(
        @SerializedName("season_number")
        var seasonNumber: Int = 0,

        @SerializedName("episode_count")
        var episodeCount: Int = 0,

        @SerializedName("poster_path")
        var posterPath: String? = null
)

data class CastResponse (
        @SerializedName("id")
        var id: Int = 0,

        @SerializedName("cast")
        var castList: List<Cast>? = null
)

