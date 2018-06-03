package com.vanillax.televisionbingecalculator.app.Kotlin.viewmodels

import android.databinding.ObservableField
import com.vanillax.televisionbingecalculator.app.Kotlin.Utils.CalculatorUtils
import com.vanillax.televisionbingecalculator.app.Kotlin.enum.SearchType
import com.vanillax.televisionbingecalculator.app.Kotlin.network.JustWatchAPIService
import com.vanillax.televisionbingecalculator.app.Kotlin.network.TheMovieDBService
import com.vanillax.televisionbingecalculator.app.Kotlin.network.response.CastResponse
import com.vanillax.televisionbingecalculator.app.Kotlin.network.response.JustWatchResponse
import com.vanillax.televisionbingecalculator.app.Kotlin.network.response.TVShowByIdResponse
import com.vanillax.televisionbingecalculator.app.R
import com.vanillax.televisionbingecalculator.app.Util.BindingAdapter.BindingTextHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import java.util.*



class DetailsViewModel(theMovieDBService: TheMovieDBService, justWatchAPIService: JustWatchAPIService)
{
    interface DetailsViewModelInterface {
        fun onFetchAllDetails ( detailsItemViewModel: DetailsItemViewModel)
        fun error( error: String?)
    }

    //Dependencies
    private var disposable: Disposable? = null
    private var listener: DetailsViewModelInterface? = null
    private val movieDBService = theMovieDBService
    private val justWatchAPIService = justWatchAPIService
    private  var calcUtils : CalculatorUtils? = null

    //Initial view values
    var showId: Int = 0
    var showTitle =  ObservableField <String>("")
    var thumbnailUrl = ObservableField <String>("")
    var seasonSelected: Int = 0
    lateinit var selectedSearchType: SearchType
    var bingeTime: String = ""
    var posterUrl =  ObservableField <String>("")
    var category = ObservableField <String>("")
    var year = ObservableField <String>("")
    var showDescription = ObservableField <String>("")
    var episodeCount:String = ""
    var episodeRuntime:String = ""

    lateinit var detailsItemViewModel2: DetailsItemViewModel


    //Databinding
    val totalBingeTimeString = ObservableField<BindingTextHelper>(BindingTextHelper(R.string.estimated_binge_time, setBingeTime(bingeTime)))
    var episodesCountString = ObservableField<BindingTextHelper>(BindingTextHelper(R.string.episode_count, setBingeTime(episodeCount)))
    var runtimeString = ObservableField<BindingTextHelper>(BindingTextHelper(R.string.runtime, setBingeTime(episodeRuntime)))


    var list = ArrayList<String>()

    fun setListener( listener: DetailsViewModelInterface ){

        this.listener = listener
    }

    //Lifecycle
    fun onDisconnect() {
        disposable?.dispose()
    }

    //setup
    //Required to initialize network calls
    //Did not want to overpopulate the constructor
    fun setShowMetaData( showId: Int,
                         showTitle: String,
                         thumbnailUrl: String,
                         selectedSearchType: SearchType)
    {
        this.showId = showId
        this.showTitle.set( showTitle)
        this.thumbnailUrl.set(thumbnailUrl)
        this.selectedSearchType = selectedSearchType
    }

//    private fun populateView( detailsItemViewModel: DetailsItemViewModel )
//    {
//        binding.episodeRuntime.text = runtime.toString() + " minutes"
//        binding.episodeTotal.text = episodeCount
//
//        binding.bingeTime.text = bingeTime
//        binding.switchToggle.isChecked = true
//    }


    //Sets total binge time text
    private fun setBingeTime(string:String) : String
    {
        return String.format(string)

    }

    private fun populateView( detailsItemViewModel: DetailsItemViewModel )
    {

        //Binge Time Text
        bingeTime = detailsItemViewModel.bingeTime.toString()
        totalBingeTimeString.set(BindingTextHelper(R.string.estimated_binge_time, setBingeTime(bingeTime)))

        posterUrl.set(detailsItemViewModel.imageUrl)
        showTitle.set(detailsItemViewModel.title)
        year.set(detailsItemViewModel.year)
        category.set(detailsItemViewModel.category)
        showDescription.set(detailsItemViewModel.showDescription)

        episodeCount= detailsItemViewModel.episodeCount.toString()
        episodesCountString.set(BindingTextHelper(R.string.episode_count,setBingeTime(episodeCount) ))

        episodeRuntime = detailsItemViewModel.runtime.toString()
        runtimeString.set(BindingTextHelper(R.string.runtime,setBingeTime(episodeRuntime) ))




        detailsItemViewModel2 = detailsItemViewModel
        listener?.onFetchAllDetails(detailsItemViewModel2)


    }

    fun selectSeason ( seasonNumber:Int )
    {
        if ( seasonNumber == 0 )
        {
            episodeCount= detailsItemViewModel2.episodeCount.toString()

            episodesCountString.set(BindingTextHelper(R.string.episode_count,setBingeTime(episodeCount) ))

            //Binge Time Text
            bingeTime = detailsItemViewModel2.bingeTime.toString()
            totalBingeTimeString.set(BindingTextHelper(R.string.estimated_binge_time, setBingeTime(bingeTime)))

            episodeRuntime = detailsItemViewModel2.runtime.toString()
            runtimeString.set(BindingTextHelper(R.string.runtime,setBingeTime(episodeRuntime) ))
        }
        else {
            episodeCount = calcUtils?.getNumberOfEpisodesForSeason(seasonNumber).toString()
            episodesCountString.set(BindingTextHelper(R.string.episode_count,setBingeTime(episodeCount) ))

            //Binge Time Text
            bingeTime = calcUtils?.calcSpecificSeason(seasonNumber).toString()
            totalBingeTimeString.set(BindingTextHelper(R.string.estimated_binge_time, setBingeTime(bingeTime)))

            episodeRuntime = detailsItemViewModel2.runtime.toString()
            runtimeString.set(BindingTextHelper(R.string.runtime,setBingeTime(episodeRuntime) ))
        }

    }



    fun getAllShowDetailsData (showId: Int, searchType: SearchType, showTitle: String)
    {
        zipAllShowDetailsData(showId,searchType,showTitle)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {result ->
                            populateView(result)
                        },
                        { error -> listener?.error(error?.message)}
                )
    }

    fun zipAllShowDetailsData( showId: Int, searchType: SearchType, showTitle: String) : Observable<DetailsItemViewModel>
    {
        return Observable.zip(
                getDetails(showId),
                getCast(searchType, showId),
                getStream(showTitle),
                Function3 { tvShowByIDResponse:TVShowByIdResponse, castResponse: CastResponse, streamDetails:JustWatchResponse ->
                    makeDetailsItemViewModel(tvShowByIDResponse,castResponse,streamDetails)
                }
        )
    }

    fun makeDetailsItemViewModel( tvShowByIdResponse: TVShowByIdResponse, castResponse: CastResponse, streamDetails:JustWatchResponse) : DetailsItemViewModel
    {
        calcUtils = com.vanillax.televisionbingecalculator.app.Kotlin.Utils.CalculatorUtils(tvShowByIdResponse)


        val episodeCount = calcUtils?.getEpisodeCount()
        val runtime = calcUtils?.getRunTimeAverage()
        val bingeTime = calcUtils?.getTotalBingeTime()
        val imageUrl = calcUtils?.getShowPosterThumbnail(tvShowByIdResponse.imageUrl,true)
        val thumbnailUrl = calcUtils?.getShowPosterThumbnail(tvShowByIdResponse.imageUrl,false)
        val category = calcUtils?.getCategory()
        val year = calcUtils?.getYear()
        val seasonsCount = calcUtils?.numberOfSeasons()

        val  vmItem = DetailsItemViewModel(
                episodeCount,
                runtime,
                bingeTime,
                imageUrl,
                thumbnailUrl,
                castResponse,
                streamDetails,
                year,
                category,
                showTitle.get().toString(),
                tvShowByIdResponse.episodeDescription,
                seasonsCount,
                tvShowByIdResponse)


        return  vmItem
    }

    fun getDetails(showId: Int): Observable<TVShowByIdResponse>
    {
        return movieDBService.queryTVDetails(showId.toString())
    }

    fun getCast( searchType: SearchType , showId: Int): Observable<CastResponse>
    {
        return  movieDBService.queryCast( showId.toString(), searchType.toString() )
    }

    fun getStream ( showTitle: String): Observable<JustWatchResponse>
    {
        return justWatchAPIService.getMovieStreamingSources(com.vanillax.televisionbingecalculator.app.Kotlin.network.response.JustWatchSearch(showTitle))
    }


}