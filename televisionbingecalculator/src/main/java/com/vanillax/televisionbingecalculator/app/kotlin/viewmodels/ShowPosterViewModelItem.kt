package com.vanillax.televisionbingecalculator.app.kotlin.viewmodels

import com.vanillax.televisionbingecalculator.app.kotlin.network.response.ShowPosterListing
import com.vanillax.televisionbingecalculator.app.kotlin.utils.CalculatorUtils

/**
 * Created by mitchross on 2/6/17.
 */

class ShowPosterViewModelItem(showPosterListing: ShowPosterListing, landingActivityViewModelInterface: LandingActivityViewModel.LandingActivityViewModelInterface)  {



    val showPosterListing = showPosterListing
    val listener = landingActivityViewModelInterface


    val titlez: String
        get() = getTitle()

    val posterUrl: String
        get() = CalculatorUtils.getShowPosterThumbnail(showPosterListing.posterPath, false)

    val id: Int
        get() = showPosterListing.id

    val score: String = "Score: " + showPosterListing.vote


    fun getTitle(): String
    {
        if ( !showPosterListing.original_name.isNullOrEmpty() )
        {
            return showPosterListing.original_name
        }
        else if ( !showPosterListing.movie_title.isNullOrEmpty())
        {
            return showPosterListing.movie_title
        }
        else
        {
            return ""
        }
    }

    fun doSomething()

    {
        listener.onTouch(showPosterListing.id,posterUrl,getTitle())

    }



}
