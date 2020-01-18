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
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.*
import com.vanillax.televisionbingecalculator.app.kotlin.utils.CalculatorUtils
import com.vanillax.televisionbingecalculator.app.udf.Resource
import com.vanillax.televisionbingecalculator.app.udf.UIEvent
import com.vanillax.televisionbingecalculator.app.viewmodel.CastViewModelItem
import com.vanillax.televisionbingecalculator.app.viewmodel.SeasonNumberViewModelItem
import com.vanillax.televisionbingecalculator.app.viewmodel.StreamingSourceViewModelItem
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

    private val detailMergedResponse = MutableLiveData<Resource<DetailMergedResponse>>().also {
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

        //TODO Add the episodeVisibility logic into the copy method here. If you do not know exactly how to format paste the logic you want here and I will add later

        _detailsViewState.value?.copy(
                loadingVisibility = if (detailMergedResponse.value is Resource.Loading) View.VISIBLE else View.GONE,
                posterUrl = detailMergedResponse.value?.data?.imageUrl.orEmpty(),
                thumbnailUrl = detailMergedResponse.value?.data?.thumbnailurl.orEmpty(),
                showTitle = detailMergedResponse.value?.data?.title.orEmpty(),
                year = detailMergedResponse.value?.data?.year.orEmpty(),
                category = detailMergedResponse.value?.data?.category.orEmpty(),
                totalBingeTimeString = detailMergedResponse.value?.data?.bingeTime.orEmpty(),
                showDescription = detailMergedResponse.value?.data?.showDescription.orEmpty(),
                episodesCountString = detailMergedResponse.value?.data?.episodeCount.toString(),
                runtimeString = detailMergedResponse.value?.data?.runtime.toString(),
                seasonCount = detailMergedResponse.value?.data?.seasonCount
                        ?: 0,
                seasonNumberItems = detailMergedResponse.value?.data?.seasonNumberItems.orEmpty(),
                justWatchResponse = detailMergedResponse.value?.data?.justWatchResponse
                        ?: JustWatchResponse(emptyList()),
                castList = detailMergedResponse.value?.data?.castList.orEmpty(),
                streamingSources = detailMergedResponse.value?.data?.streamingSources.orEmpty(),
                detailsUIEvent = uiEvent.value
        )?.let {
            _detailsViewState.value = it
        }
    }

    fun onAction(action: DetailsAction) {

        when (action) {

            is DetailsAction.FetchAllShowDetails -> {
                fetchAllData(showId = action.showId, searchType = action.searchType, showTitle = action.showTitle)
            }

            is DetailsAction.SeasonNumberClicked -> {

                detailMergedResponse.value?.data?.let {

                    it.seasonCount?.let { seasonCount ->
                        detailMergedResponse.value?.data?.copy(seasonNumberItems = createSeasonNumberItems(seasonCount, action.seasonNumber))
                                .also { mergeResponse ->
                                    detailMergedResponse.postValue(Resource.Success(mergeResponse))
                                }
                    }
                }
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            detailMergedResponse.postValue(Resource.Success(it))
                        },
                        { error ->
                            detailMergedResponse.postValue(Resource.Failure(detailMergedResponse.value?.data, error))
                            Log.d(this.javaClass.simpleName, error?.message)
                        }
                ).also {
                    disposables.add(it)
                }

    }

    private fun makeDetailsItemViewModel(tvShowByIdResponse: TVShowByIdResponse,
                                         castResponse: CastResponse,
                                         justWatchResponse: JustWatchResponse,
                                         selectedSearchType: SearchType,
                                         showTitle: String): DetailMergedResponse {

        with(CalculatorUtils(tvShowByIdResponse))
        {
            val seasonCount = numberOfSeasons()

            return DetailMergedResponse(
                    episodeCount = getEpisodeCount(),
                    runtime = getRunTimeAverage(selectedSearchType),
                    bingeTime = getTotalBingeTime(selectedSearchType),
                    imageUrl = getShowPosterThumbnail(tvShowByIdResponse.imageUrl, false),
                    thumbnailurl = getShowPosterThumbnail(tvShowByIdResponse.imageUrl, false),
                    justWatchResponse = justWatchResponse,
                    year = getYear(selectedSearchType),
                    category = getCategory(),
                    title = showTitle,
                    showDescription = tvShowByIdResponse.episodeDescription,
                    seasonCount = seasonCount,
                    tvShowByIdResponse = tvShowByIdResponse,
                    seasonNumberItems = createSeasonNumberItems(seasons = seasonCount
                            ?: 0, seasonToSelect = 0),
                    castList = createCastListItems(castResponse),
                    streamingSources = createStreamingSourceItems(showTitle, justWatchResponse))

        }
    }

    private fun createSeasonNumberItems(seasons: Int,
                                        seasonToSelect: Int): List<SeasonNumberViewModelItem> {

        val mutableList = mutableListOf<SeasonNumberViewModelItem>()

        for (i in 0 until seasons + 1) {
            mutableList.add(SeasonNumberViewModelItem(i, i == seasonToSelect, this::onAction))
        }

        return mutableList
    }

    private fun createCastListItems(castResponse: CastResponse): List<CastViewModelItem> {
        return castResponse.castList?.map {
            CastViewModelItem(it)
        }.orEmpty()
    }

    private fun createStreamingSourceItems(showTitle: String,
                                           justWatchResponse: JustWatchResponse): List<StreamingSourceViewModelItem> {

        if (justWatchResponse.items != null || justWatchResponse.items!!.size != 0) {

            return justWatchResponse.items.first {
                it.title?.contains(showTitle) == true
            }.offers?.distinct()?.map {

                when (it.providerId) {

                    Offer.Streamtype.VUDU -> {
                        StreamingSourceViewModelItem(StreamingSourceViewModelItem.VideoStream.VUDO)
                    }

                    Offer.Streamtype.NETFLIX -> {
                        StreamingSourceViewModelItem(StreamingSourceViewModelItem.VideoStream.NETFLIX)
                    }

                    Offer.Streamtype.AMAZON -> {
                        StreamingSourceViewModelItem(StreamingSourceViewModelItem.VideoStream.AMAZON)
                    }

                    Offer.Streamtype.HBO -> {
                        StreamingSourceViewModelItem(StreamingSourceViewModelItem.VideoStream.HBO)
                    }

                    Offer.Streamtype.HULU -> {
                        StreamingSourceViewModelItem(StreamingSourceViewModelItem.VideoStream.HULU)
                    }

                    else -> {
                        StreamingSourceViewModelItem(null)
                    }
                }

            }.orEmpty()
        }

        return emptyList()
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
                                val seasonCount: Int = 0,
                                val seasonNumberItems: List<SeasonNumberViewModelItem> = emptyList(),
                                val justWatchResponse: JustWatchResponse = JustWatchResponse(emptyList()),
                                val castList: List<CastViewModelItem> = emptyList(),
                                val streamingSources: List<StreamingSourceViewModelItem> = emptyList(),
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

    data class DetailMergedResponse
    (
            val episodeCount: Int?,
            val runtime: Int?,
            val bingeTime: String?,
            val imageUrl: String?,
            val thumbnailurl: String?,
            val justWatchResponse: JustWatchResponse,
            val year: String?,
            val category: String?,
            val title: String?,
            val showDescription: String?,
            val seasonCount: Int?,
            val tvShowByIdResponse: TVShowByIdResponse?,
            val seasonNumberItems: List<SeasonNumberViewModelItem>,
            val castList: List<CastViewModelItem>,
            val streamingSources: List<StreamingSourceViewModelItem>
    )
}