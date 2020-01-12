package com.vanillax.televisionbingecalculator.app.kotlin.viewmodels

import android.util.Log
import android.view.View
import com.vanillax.televisionbingecalculator.app.kotlin.enum.SearchType
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vanillax.televisionbingecalculator.app.kotlin.network.JustWatchAPIService
import com.vanillax.televisionbingecalculator.app.kotlin.network.TheMovieDBService
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.CastResponse
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.JustWatchResponse
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.JustWatchSearch
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.TVShowByIdResponse
import com.vanillax.televisionbingecalculator.app.kotlin.utils.CalculatorUtils
import com.vanillax.televisionbingecalculator.app.udf.Resource
import com.vanillax.televisionbingecalculator.app.udf.UIEvent
import com.vanillax.televisionbingecalculator.app.viewmodel.SeasonNumberViewModelItem
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.functions.Function3

class DetailsViewModel2(private val movieDBService: TheMovieDBService,
                        private val justWatchAPIService: JustWatchAPIService) : ViewModel() {

    private val _detailsViewState = MediatorLiveData<DetailsViewState>()
            .also { it.value = DetailsViewState() }
    val detailsViewState: LiveData<DetailsViewState> = _detailsViewState

    // It seems like this is the class that really controls the page but I would change the name.
    // You have a details view model and this is really just another ViewState class

    // If you feel like DetailsViewState is getting to large break it up

    // DetailsViewState can contain other viewStates as well we leave this up to the developer if it makes sense.

    private val detailsItemViewModel = MutableLiveData<Resource<DetailsItemViewModel>>().also {
        _detailsViewState.addSource(it) {
            combineSources()
        }
    }

    private val uiEvent = MutableLiveData<UIEvent<DetailsUIEvent>>()
            .also {
                _detailsViewState.addSource(it) {
                    combineSources()
                }
            }

    private val disposables = CompositeDisposable()

    // WHEN ADDING THE REST OF THE copy() values add them in order of the ViewState constructor
    // for consistency sake

    private fun combineSources() {
        _detailsViewState.value?.copy(
                loadingVisibility = if (detailsItemViewModel.value is Resource.Loading) View.VISIBLE else View.GONE,
                detailsItemViewModel = detailsItemViewModel.value?.data,
                detailsUIEvent = uiEvent.value
        )?.let {
            _detailsViewState.value = it
        }
    }

    fun onAction(action: DetailsAction) {

        when (action) {

            is DetailsAction.FetchAllShowDetails -> {

            }

            is DetailsAction.SeasonNumberClicked -> {

            }
        }
    }

    private fun fetchAllData(showId: Int,
                             searchType: SearchType,
                             showTitle: String) {

        val queryByIdSingle = searchType.run {

            if (this == SearchType.TV) {
                movieDBService.queryTVDetails(showId.toString())
            } else {
                movieDBService.queryMovieDetails(showId.toString())
            }

        }.subscribeOn(Schedulers.io())

        val queryCastSingle = searchType.run {

            if (this == SearchType.TV) {
                movieDBService.queryCast(showId.toString(), searchType.toString())
            } else {
                movieDBService.queryMovieCast(showId.toString())
            }

        }.subscribeOn(Schedulers.io())

        val justWatchSingle =
                justWatchAPIService.getMovieStreamingSources(JustWatchSearch(showTitle)).subscribeOn(Schedulers.io())

        Single.zip(
                queryByIdSingle,
                queryCastSingle,
                justWatchSingle,
                Function3 { tvShowByIDResponse: TVShowByIdResponse, castResponse: CastResponse, streamDetails: JustWatchResponse ->
                    makeDetailsItemViewModel(tvShowByIDResponse, castResponse, streamDetails, searchType, showTitle)
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->

                        },
                        { error ->
                            Log.d(this.javaClass.simpleName, error?.message)
                        }
                ).also {
                    disposables.add(it)
                }

    }

    private fun makeDetailsItemViewModel(tvShowByIdResponse: TVShowByIdResponse,
                                         castResponse: CastResponse,
                                         streamDetails: JustWatchResponse,
                                         selectedSearchType: SearchType,
                                         showTitle: String): DetailsItemViewModel {

        val calcUtils = CalculatorUtils(tvShowByIdResponse)

        val episodeCount = calcUtils.getEpisodeCount()
        val runtime = calcUtils.getRunTimeAverage(selectedSearchType)
        val bingeTime = calcUtils.getTotalBingeTime(selectedSearchType)
        val imageUrl = calcUtils.getShowPosterThumbnail(tvShowByIdResponse.imageUrl, false)
        val thumbnailUrl = calcUtils.getShowPosterThumbnail(tvShowByIdResponse.imageUrl, false)
        val category = calcUtils.getCategory()
        val year = calcUtils.getYear(selectedSearchType)
        val seasonsCount = calcUtils.numberOfSeasons()

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
                showTitle,
                tvShowByIdResponse.episodeDescription,
                seasonsCount,
                tvShowByIdResponse)
    }

    data class DetailsViewState(val loadingVisibility: Int = View.GONE,
                                val posterUrl: String = "",
                                val thumbnailUrl: String = "",
                                val showTitle: String = "",
                                val year: String = "",
                                val category: String = "",
                                val totalBingeTimeString: String = "",
                                val showDescription: String = "",
                                val episodesCountString: String = "",
                                val episodeVisibility: Int = View.GONE,
                                val runtimeString: String = "",
                                val seasonCount: MutableList<SeasonNumberViewModelItem> = mutableListOf(),
                                val detailsItemViewModel: DetailsItemViewModel? = null,
                                val detailsUIEvent: UIEvent<DetailsUIEvent>? = null)

    sealed class DetailsAction {
        data class FetchAllShowDetails(val showId: Int,
                                       val searchType: SearchType,
                                       val showTitle: String) : DetailsAction()

        data class SeasonNumberClicked(val seasonNumber: Int) : DetailsAction()
    }


    /*
        USE DetailsUIEvent if you have a one time event
        aka

        If you have a snack bar that pops up and when you navigate away and back to this screen it does not appear again. 
        but you can trigger it as often as you would like

        And create the events like so -> UIEvent.create(FakeEvent)
     */


    sealed class DetailsUIEvent {
        object FakeEvent : DetailsUIEvent()
    }

}