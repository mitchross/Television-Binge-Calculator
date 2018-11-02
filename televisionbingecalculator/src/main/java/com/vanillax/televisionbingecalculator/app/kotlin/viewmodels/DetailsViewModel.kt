package com.vanillax.televisionbingecalculator.app.kotlin.viewmodels

import android.util.Log
import androidx.databinding.ObservableField
import com.vanillax.televisionbingecalculator.app.R
import com.vanillax.televisionbingecalculator.app.kotlin.enum.SearchType
import com.vanillax.televisionbingecalculator.app.kotlin.network.JustWatchAPIService
import com.vanillax.televisionbingecalculator.app.kotlin.network.TheMovieDBService
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.CastResponse
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.JustWatchResponse
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.JustWatchSearch
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.TVShowByIdResponse
import com.vanillax.televisionbingecalculator.app.kotlin.utils.CalculatorUtils
import com.vanillax.televisionbingecalculator.app.util.bindingadapter.BindingTextHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import java.util.*





class DetailsViewModel(theMovieDBService: TheMovieDBService, justWatchAPIService: JustWatchAPIService)
{
    interface DetailsViewModelInterface {
        fun onFetchAllDetails ( detailsItemViewModel: DetailsItemViewModel)
        fun error( error: String?)
        fun onSeasonNumberTouch( seasonNumber: Int )

    }

    //Dependencies
   // private var disposable: Disposable? = null
    private var disposables = CompositeDisposable()


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
    var isMovie = ObservableField <Boolean> ( false)
    var isLoading  = ObservableField <Boolean> ( true)
    var bingeTime: String = ""
    var posterUrl =  ObservableField <String>("")
    var category = ObservableField <String>("")
    var year = ObservableField <String>("")
    var showDescription = ObservableField <String>("")
    var episodeCount:String = ""
    var episodeRuntime:String = ""

    var detailsItemViewModel: DetailsItemViewModel? = null


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
        disposables?.clear()

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

        when (selectedSearchType) {
            SearchType.MOVIE -> isMovie.set(true)
            else -> isMovie.set(false)
        }
    }




    //Sets total binge time text
    private fun setBingeTime(string:String) : String
    {
        return String.format(string)

    }

    private fun populateView( detailsItemViewModel: DetailsItemViewModel )
    {
        isLoading.set(false)

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




        listener?.onFetchAllDetails(detailsItemViewModel)


    }

    fun selectSeason ( seasonNumber:Int )
    {
        if ( seasonNumber == 0 )
        {
            episodeCount= detailsItemViewModel?.episodeCount.toString()

            episodesCountString.set(BindingTextHelper(R.string.episode_count,setBingeTime(episodeCount) ))

            //Binge Time Text
            bingeTime = detailsItemViewModel?.bingeTime.toString()
            totalBingeTimeString.set(BindingTextHelper(R.string.estimated_binge_time, setBingeTime(bingeTime)))

            episodeRuntime = detailsItemViewModel?.runtime.toString()
            runtimeString.set(BindingTextHelper(R.string.runtime,setBingeTime(episodeRuntime) ))
        }
        else {
            episodeCount = calcUtils?.getNumberOfEpisodesForSeason(seasonNumber).toString()
            episodesCountString.set(BindingTextHelper(R.string.episode_count,setBingeTime(episodeCount) ))

            //Binge Time Text
            bingeTime = calcUtils?.calcSpecificSeason(seasonNumber,selectedSearchType).toString()
            totalBingeTimeString.set(BindingTextHelper(R.string.estimated_binge_time, setBingeTime(bingeTime)))

            episodeRuntime = detailsItemViewModel?.runtime.toString()
            runtimeString.set(BindingTextHelper(R.string.runtime,setBingeTime(episodeRuntime) ))
        }

    }



    fun getAllShowDetailsData (showId: Int, searchType: SearchType, showTitle: String)
    {
        isLoading.set(true)

        disposables.clear()
        disposables.add(zipAllShowDetailsData(showId,searchType,showTitle)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result ->
                            populateView(result)
                        },
                        {
                            error -> listener?.error(error?.message)
                            isLoading.set(false)
                        }
                )
        )
    }

    fun zipAllShowDetailsData( showId: Int, searchType: SearchType, showTitle: String) : Observable<DetailsItemViewModel>
    {
        return Observable.zip(
                getDetails(showId).subscribeOn(Schedulers.io()),
                getCast(searchType, showId).subscribeOn(Schedulers.io()),
                getStream(showTitle).subscribeOn(Schedulers.io()),
                Function3 { tvShowByIDResponse: TVShowByIdResponse, castResponse: CastResponse, streamDetails: JustWatchResponse ->
                    makeDetailsItemViewModel(tvShowByIDResponse,castResponse,streamDetails)
                }
        )
    }

    fun makeDetailsItemViewModel( tvShowByIdResponse: TVShowByIdResponse, castResponse: CastResponse, streamDetails:JustWatchResponse) : DetailsItemViewModel
    {
        calcUtils = CalculatorUtils(tvShowByIdResponse)


        val episodeCount = calcUtils?.getEpisodeCount()
        val runtime = calcUtils?.getRunTimeAverage(selectedSearchType)
        val bingeTime = calcUtils?.getTotalBingeTime(selectedSearchType)
        val imageUrl = calcUtils?.getShowPosterThumbnail(tvShowByIdResponse.imageUrl,false)
        val thumbnailUrl = calcUtils?.getShowPosterThumbnail(tvShowByIdResponse.imageUrl,false)
        val category = calcUtils?.getCategory()
        val year = calcUtils?.getYear(selectedSearchType)
        val seasonsCount = calcUtils?.numberOfSeasons()

          detailsItemViewModel = DetailsItemViewModel(
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


        return detailsItemViewModel as DetailsItemViewModel
    }

    fun getDetails(showId: Int): Observable<TVShowByIdResponse>
    {


        when (selectedSearchType) {
            SearchType.TV -> return movieDBService.queryTVDetails(showId.toString())
            else -> return movieDBService.queryMovieDetails(showId.toString()).subscribeOn(Schedulers.io()).doOnSubscribe { Log.d("test", "1Thread id: "  + Thread.currentThread().getId())
             }
        }
    }

    fun getCast( searchType: SearchType , showId: Int): Observable<CastResponse>
    {
        when( selectedSearchType) {
            SearchType.TV -> return movieDBService.queryCast( showId.toString(), searchType.toString() )
            else -> return movieDBService.queryMovieCast( showId.toString() ).subscribeOn(Schedulers.io()).doOnSubscribe { Log.d("test", "2Thread id: "  + Thread.currentThread().getId())
             }
        }

    }

    fun getStream ( showTitle: String): Observable<JustWatchResponse>
    {
        return justWatchAPIService.getMovieStreamingSources(JustWatchSearch(showTitle)).subscribeOn(Schedulers.io()).doOnSubscribe { Log.d("test", "3Thread id: "  + Thread.currentThread().getId())
         }
    }


}