package com.vanillax.televisionbingecalculator.app.kotlin.viewmodels

import com.vanillax.televisionbingecalculator.app.kotlin.network.response.CastResponse
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.JustWatchResponse
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.TVShowByIdResponse

data class DetailsItemViewModel
(
        val episodeCount: Int?,
        val runtime: Int?,
        val bingeTime: String?,
        val imageUrl: String?,
        val thumbnailurl: String?,
        val castResponse: CastResponse,
        val justWatchResponse: JustWatchResponse,
        val year: String?,
        val category: String?,
        val title: String?,
        val showDescription: String?,
        val seasonCount: Int?,
        val tvShowByIdResponse: TVShowByIdResponse?
)