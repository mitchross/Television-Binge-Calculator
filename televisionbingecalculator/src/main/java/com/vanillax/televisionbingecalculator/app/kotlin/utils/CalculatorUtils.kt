package com.vanillax.televisionbingecalculator.app.kotlin.utils

import android.content.Context
import com.vanillax.televisionbingecalculator.app.kotlin.enum.SearchType
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.TVShowByIdResponse
import com.vanillax.televisionbingecalculator.app.tbc.Constants
import roboguice.util.Ln
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CalculatorUtils(internal var tvShowByIdResponse: TVShowByIdResponse) {


    fun getRunTimeAverage(searchType: SearchType): Int {

        if  ( searchType == SearchType.TV ) {

            if (tvShowByIdResponse.episodeRunTimeArray == null) {
                return 0
            }

            if (tvShowByIdResponse.episodeRunTimeArray.isEmpty()) {
                return 0
            }
            var sum = 0
            tvShowByIdResponse.episodeRunTimeArray?.forEach { i -> sum += i }

            return sum / tvShowByIdResponse.episodeRunTimeArray.size
        }
        else {
            return tvShowByIdResponse.movie_runtime

        }

    }

    fun numberOfSeasons(): Int? {

        if ( tvShowByIdResponse.seasons == null )
        {
            return 0
        }

        return if (tvShowByIdResponse.seasons?.size == 1) {
            1
        } else {
            tvShowByIdResponse.seasons?.size?.minus(1)
        }
    }

     fun getEpisodeCount(): Int {
        var totalEpisodes = 0


         tvShowByIdResponse.seasons?.forEach { s ->
             Ln.d("Season " + s.seasonNumber + "Size " + s.episodeCount)
             if (s.seasonNumber != 0) {
                 totalEpisodes += s.episodeCount
             }
         }


        return totalEpisodes
    }

    fun getRunTimeForSeason(seasonNumber: Int, searchType: SearchType): Int {
        val totalEpisodes = getNumberOfEpisodesForSeason(seasonNumber)

        return totalEpisodes * getRunTimeAverage(searchType)

    }

    fun getNumberOfEpisodesForSeason(seasonNumber: Int): Int {
        tvShowByIdResponse.seasons?.forEach { s ->
            if (s.seasonNumber == seasonNumber) {
                return s.episodeCount
            }
        }
        return 0
    }

    fun getYear(searchType: SearchType): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd")
        try {
            var date = Date()

            when (searchType) {
                SearchType.TV -> date = inputFormat.parse(if (tvShowByIdResponse.firstAirDate != null) tvShowByIdResponse.firstAirDate else "")
                else -> date = inputFormat.parse(if (tvShowByIdResponse.movie_release_date != null) tvShowByIdResponse.movie_release_date else "")
            }

            val c = Calendar.getInstance()
            c.time = date
            return c.get(Calendar.YEAR).toString()
        } catch (e: ParseException) {
            e.printStackTrace()
            return ""
        }


    }

    fun getMovieYear(): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd")
        try {
            var date = Date()
            val c = Calendar.getInstance()
            c.time = date
            return c.get(Calendar.YEAR).toString()
        } catch (e: ParseException) {
            e.printStackTrace()
            return ""
        }


    }

    fun getMovieRunTime(): String {
        return tvShowByIdResponse.movie_runtime.toString()
    }


    fun getCategory(): String {
        //return "";
        return if (tvShowByIdResponse.genres != null) {
            //TODO FIX THIS
            tvShowByIdResponse.genres!!.get(0).name

        } else {
            ""
        }
    }

    //Maths

    fun getTotalBingeTime(searchType: SearchType): String {
        when (searchType) {
            SearchType.TV -> {
                val runTime = getRunTimeAverage(searchType)
                val totalEpisodes = getEpisodeCount()
                val totalBingTime = runTime * totalEpisodes
                return convertToDaysHoursMins(totalBingTime)
            }
            else -> return convertToDaysHoursMins( tvShowByIdResponse.movie_runtime )
        }

    }



    fun calcFineTuneTime(context: Context, totalEpisodes: Int, runtime: Int, openingCreditTime: Int, closingCreditTime: Int, hasCommercials: Boolean): String {
        var newRunTime = runtime


        //Since we are taking out commercials use best guess
        if (hasCommercials) {
            if (newRunTime == 30) {
                newRunTime = 22
            } else if (newRunTime == 60) {
                newRunTime = 45
            }

        }

        val runtimeMinusCredits = newRunTime - (openingCreditTime + closingCreditTime)

        val totalBingTime = runtimeMinusCredits * totalEpisodes

        return convertToDaysHoursMins( totalBingTime)

    }

    fun calcSpecificSeason( seasonNumber: Int, searchType: SearchType): String {


        return convertToDaysHoursMins( getRunTimeForSeason(seasonNumber, searchType) )

    }


    fun convertToDaysHoursMins(timeInMinutes: Int): String {
        val minutes: Double
        val hours: Double
        val days: Double
        //val resources = context.resources


        days = Math.floor((timeInMinutes / 1440).toDouble())
        val temp = timeInMinutes - days * 1440
        hours = Math.floor(temp / 60)
        minutes = temp - hours * 60
//
//        val daysText = resources.getQuantityString(R.plurals.day, days.toInt(), days.toInt())
//        val hoursText = resources.getQuantityString(R.plurals.hours, hours.toInt(), hours.toInt())
//        val minsText = resources.getQuantityString(R.plurals.mins, minutes.toInt(), minutes.toInt())
        var daysText = days.toInt().toString() + "d"
        val hoursText = hours.toInt().toString() + "h"
        val minsText = minutes.toInt().toString() + "m"


        return "$daysText $hoursText $minsText"


    }






    //TODO figure this out.. static?
    companion object{

        fun getShowPosterThumbnail(path: String?, large: Boolean): String {
            return if (path != null) {
                (if (large) Constants.TVBC_Constants.BASE_IMAGE_PATH_LARGE else Constants.TVBC_Constants.BASE_IMAGE_PATH) + path
            } else ""
        }

    }

    fun getShowPosterThumbnail(path: String?, large: Boolean): String {
        return if (path != null) {
            (if (large) Constants.TVBC_Constants.BASE_IMAGE_PATH_LARGE else Constants.TVBC_Constants.BASE_IMAGE_PATH) + path
        } else ""
    }


//    fun getShowPosterThumbnail( large: Boolean ): String {
//        return if (tvShowByIdResponse.imageUrl != null) {
//            (if (large) TBCModule.BASE_IMAGE_PATH_LARGE else TBCModule.BASE_IMAGE_PATH) + tvShowByIdResponse.imageUrl
//        } else ""
//    }




}