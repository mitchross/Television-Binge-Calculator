package com.vanillax.televisionbingecalculator.app.kotlin.viewmodels

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import java.util.*

//TODO make this use LiveData and ViewModel and remove orientation this screen might need seperate landscape layout.
class DetailsViewModel(
        theMovieDBService: TheMovieDBService,
        justWatchAPIService: JustWatchAPIService
) : ViewModel(),DefaultLifecycleObserver

{

    private val _detailsItemViewModel = MutableLiveData<DetailsItemViewModel>()
     val detailsItemViewModel: LiveData<DetailsItemViewModel>
        get() =_detailsItemViewModel




    //Dependencies
    // private var disposable: Disposable? = null
    private var disposables = CompositeDisposable()



    private val movieDBService = theMovieDBService
    private val justWatchAPIService = justWatchAPIService
    private var calcUtils: CalculatorUtils? = null

    //Initial view values
    var showId: Int = 0
    var showTitle = ObservableField<String>("")
    var thumbnailUrl = ObservableField<String>("")
    var seasonSelected: Int = 0
    lateinit var selectedSearchType: SearchType
    var isMovie = ObservableField<Boolean>(false)
    var isLoading = ObservableField<Boolean>(true)
    var bingeTime: String = ""
    var posterUrl = ObservableField<String>("")
    var category = ObservableField<String>("")
    var year = ObservableField<String>("")
    var showDescription = ObservableField<String>("")
    var episodeCount: String = ""
    var episodeRuntime: String = ""




    //Databinding
    val totalBingeTimeString = ObservableField<BindingTextHelper>(BindingTextHelper(R.string.estimated_binge_time, setBingeTime(bingeTime)))
    var episodesCountString = ObservableField<BindingTextHelper>(BindingTextHelper(R.string.episode_count, setBingeTime(episodeCount)))
    var runtimeString = ObservableField<BindingTextHelper>(BindingTextHelper(R.string.runtime, setBingeTime(episodeRuntime)))


    var list = ArrayList<String>()



    //Lifecycle
    fun onDisconnect() {
        disposables?.clear()

    }


    //setup
    //Required to initialize network calls
    //Did not want to overpopulate the constructor
    fun setShowMetaData(showId: Int,
                        showTitle: String,
                        thumbnailUrl: String,
                        selectedSearchType: SearchType) {
        this.showId = showId
        this.showTitle.set(showTitle)
        this.thumbnailUrl.set(thumbnailUrl)
        this.selectedSearchType = selectedSearchType

        when (selectedSearchType) {
            SearchType.MOVIE -> isMovie.set(true)
            else -> isMovie.set(false)
        }
    }


    //Sets total binge time text
    private fun setBingeTime(string: String): String {
        return String.format(string)

    }

     fun populateView(detailsItemViewModel: DetailsItemViewModel) {
        isLoading.set(false)

        //Binge Time Text
        bingeTime = detailsItemViewModel.bingeTime.toString()
        totalBingeTimeString.set(BindingTextHelper(R.string.estimated_binge_time, setBingeTime(bingeTime)))

        posterUrl.set(detailsItemViewModel.imageUrl)
        showTitle.set(detailsItemViewModel.title)
        year.set(detailsItemViewModel.year)
        category.set(detailsItemViewModel.category)
        showDescription.set(detailsItemViewModel.showDescription)

        episodeCount = detailsItemViewModel.episodeCount.toString()
        episodesCountString.set(BindingTextHelper(R.string.episode_count, setBingeTime(episodeCount)))

        episodeRuntime = detailsItemViewModel.runtime.toString()
        runtimeString.set(BindingTextHelper(R.string.runtime, setBingeTime(episodeRuntime)))





    }

    fun toggleOffOtherSeasons(seasonNumber: Int) {

    }

    fun selectSeason(seasonNumber: Int) {

        //TODO is this right?

        if (seasonNumber == 0) {
            _detailsItemViewModel.value?.let{

                episodeCount = it.episodeCount.toString()
                episodesCountString.set(BindingTextHelper(R.string.episode_count, setBingeTime(episodeCount)))


                bingeTime = it.bingeTime.toString()
                totalBingeTimeString.set(BindingTextHelper(R.string.estimated_binge_time, setBingeTime(bingeTime)))

                episodeRuntime = it.runtime.toString()
                runtimeString.set(BindingTextHelper(R.string.runtime, setBingeTime(episodeRuntime)))



            }


        } else {

            _detailsItemViewModel.value?.let {

               episodeRuntime=  it.runtime.toString()
                runtimeString.set(BindingTextHelper(R.string.runtime, setBingeTime(episodeRuntime)))
            }

            episodeCount = calcUtils?.getNumberOfEpisodesForSeason(seasonNumber).toString()
            episodesCountString.set(BindingTextHelper(R.string.episode_count, setBingeTime(episodeCount)))

            //Binge Time Text
            bingeTime = calcUtils?.calcSpecificSeason(seasonNumber, selectedSearchType).toString()
            totalBingeTimeString.set(BindingTextHelper(R.string.estimated_binge_time, setBingeTime(bingeTime)))

        }

    }


    fun getAllShowDetailsData(showId: Int, searchType: SearchType, showTitle: String) {
        isLoading.set(true)

        disposables.clear()
        disposables.add(zipAllShowDetailsData(showId, searchType, showTitle)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                           // populateView(result)
                            _detailsItemViewModel.postValue( result )

                        },
                        { error ->
                            Log.d(this.javaClass.simpleName,error?.message)
                            isLoading.set(false)
                        }
                )
        )
    }

    fun zipAllShowDetailsData(showId: Int, searchType: SearchType, showTitle: String): Single<DetailsItemViewModel> {
        return Single.zip(
                getDetails(showId).subscribeOn(Schedulers.io()),
                getCast(searchType, showId).subscribeOn(Schedulers.io()),
                getStream(showTitle).subscribeOn(Schedulers.io()),
                Function3 { tvShowByIDResponse: TVShowByIdResponse, castResponse: CastResponse, streamDetails: JustWatchResponse ->
                    makeDetailsItemViewModel(tvShowByIDResponse, castResponse, streamDetails)
                }
        )
    }

    fun makeDetailsItemViewModel(tvShowByIdResponse: TVShowByIdResponse, castResponse: CastResponse, streamDetails: JustWatchResponse): DetailsItemViewModel {
        calcUtils = CalculatorUtils(tvShowByIdResponse)


        val episodeCount = calcUtils?.getEpisodeCount()
        val runtime = calcUtils?.getRunTimeAverage(selectedSearchType)
        val bingeTime = calcUtils?.getTotalBingeTime(selectedSearchType)
        val imageUrl = calcUtils?.getShowPosterThumbnail(tvShowByIdResponse.imageUrl, false)
        val thumbnailUrl = calcUtils?.getShowPosterThumbnail(tvShowByIdResponse.imageUrl, false)
        val category = calcUtils?.getCategory()
        val year = calcUtils?.getYear(selectedSearchType)
        val seasonsCount = calcUtils?.numberOfSeasons()


        return DetailsItemViewModel(
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
    }

    fun getDetails(showId: Int): Single<TVShowByIdResponse> {


        when (selectedSearchType) {
            SearchType.TV -> return movieDBService.queryTVDetails(showId.toString())
            else -> return movieDBService.queryMovieDetails(showId.toString()).subscribeOn(Schedulers.io()).doOnSubscribe {
                Log.d("test", "1Thread id: " + Thread.currentThread().getId())
            }
        }
    }

    fun getCast(searchType: SearchType, showId: Int): Single<CastResponse> {
        when (selectedSearchType) {
            SearchType.TV -> return movieDBService.queryCast(showId.toString(), searchType.toString())
            else -> return movieDBService.queryMovieCast(showId.toString()).subscribeOn(Schedulers.io()).doOnSubscribe {
                Log.d("test", "2Thread id: " + Thread.currentThread().getId())
            }
        }

    }

    fun getStream(showTitle: String): Single<JustWatchResponse> {
        return justWatchAPIService.getMovieStreamingSources(JustWatchSearch(showTitle)).subscribeOn(Schedulers.io()).doOnSubscribe {
            Log.d("test", "3Thread id: " + Thread.currentThread().getId())
        }
    }


}