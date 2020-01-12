package com.vanillax.televisionbingecalculator.app.kotlin.viewmodels

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vanillax.televisionbingecalculator.app.kotlin.enum.SearchType
import com.vanillax.televisionbingecalculator.app.kotlin.network.TVBCLoggerService
import com.vanillax.televisionbingecalculator.app.kotlin.network.TheMovieDBService
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.QueryResponse
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.ShowPosterListing
import com.vanillax.televisionbingecalculator.app.serverapi.TVBCLogger.SearchTerm
import com.vanillax.televisionbingecalculator.app.udf.UIEvent
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by mitchross on 4/14/18.
 */
class LandingActivityViewModel(
        private val service: TheMovieDBService,
        private val tvbcLoggerService: TVBCLoggerService
) : ViewModel() {

    private val _landingViewState = MediatorLiveData<LandingViewState>()
            .also { it: MediatorLiveData<LandingViewState> ->
                it.value = LandingViewState()
            }

    val landingViewState: LiveData<LandingViewState> = _landingViewState

    private val searchQuery = MutableLiveData<String>()
            .also {
                _landingViewState.addSource(it) {
                    combineSources()
                }
            }

    private val queryResponse = MutableLiveData<Resource<QueryResponse>>()
            .also {
                _landingViewState.addSource(it) {
                    combineSources()
                }
                it.value = Resource.Success(
                        QueryResponse(
                                _landingViewState.value!!.showPosterListings,
                                _landingViewState.value!!.searchType))
            }

    private val uiEvent = MutableLiveData<UIEvent<LandingEvent>>()
            .also {
                _landingViewState.addSource(it) {
                    combineSources()
                }
            }

    private var compositeDisposable = CompositeDisposable()

    private fun combineSources() {
        _landingViewState.value?.copy(
                showPosterListings = queryResponse.value?.data?.showPosterListing.orEmpty(),
                searchType = queryResponse.value?.data?.searchType!!,
                movieSelectionVisibility = if (queryResponse.value?.data?.searchType == SearchType.MOVIE) View.VISIBLE else View.GONE,
                tvSelectionVisibility = if (queryResponse.value?.data?.searchType == SearchType.TV) View.VISIBLE else View.GONE,
                searchQuery = searchQuery.value.orEmpty(),
                landingEvent = uiEvent.value,
                hasShowsVisibility = if (queryResponse.value?.data?.showPosterListing.isNullOrEmpty()) View.GONE else View.VISIBLE,
                emptyShowVisibility = if (queryResponse.value?.data?.showPosterListing.isNullOrEmpty()) View.VISIBLE else View.GONE
        )?.let {
            _landingViewState.value = it
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun onAction(action: LandingAction) {

        when (action) {

            is LandingAction.MovieClicked -> {
                search(SearchType.MOVIE)
            }

            is LandingAction.TelevisionClicked -> {
                search(SearchType.TV)
            }

            is LandingAction.Search -> {
                searchQuery.value = action.searchQuery
                search(queryResponse.value?.data?.searchType!!)
            }

            is LandingAction.NavigateToDetails -> {

                action.title?.let {

                    _landingViewState.value?.searchType?.let {

                        when (it) {
                            SearchType.MOVIE -> "Movie: $it"
                            SearchType.TV -> "TV: $it"
                        }.run {

                            tvbcLoggerService.postSearchTerm(SearchTerm(this))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({
                                        Log.d(this.javaClass.simpleName, "Post search term call completed")
                                    }, {
                                        //no-op
                                    })
                        }

                        uiEvent.postValue(UIEvent.create(LandingEvent.ShowDetails(action.id, action.url, action.title.orEmpty(), it)))
                    }
                }
            }
        }
    }

    private fun search(searchType: SearchType) {

        if (searchQuery.value.isNullOrEmpty()) {
            queryResponse.postValue(Resource.Stale(queryResponse.value?.data?.copy(searchType = searchType)))
            return
        }

        when (searchType) {

            SearchType.TV -> {
                service.queryTV(searchQuery.value!!)
                        .reusableSingleQuery(searchType)
            }

            SearchType.MOVIE -> {
                service.queryMovie(searchQuery.value!!)
                        .reusableSingleQuery(searchType)
            }
        }
    }

    private fun Single<QueryResponse>.reusableSingleQuery(searchType: SearchType) {
        this.subscribeOn(Schedulers.io())
                .subscribe({
                    queryResponse.postValue(Resource.Success(it.apply {
                        this.searchType = searchType
                    }))

                }, {
                    Log.d(this.javaClass.simpleName, it.message.orEmpty())
                }).also {
                    compositeDisposable.add(it)
                }
    }

    data class LandingViewState(val showPosterListings: List<ShowPosterListing> = emptyList(),
                                val searchType: SearchType = SearchType.TV,
                                val tvSelectionVisibility: Int = View.VISIBLE,
                                val movieSelectionVisibility: Int = View.GONE,
                                val emptyShowVisibility: Int = View.VISIBLE,
                                val hasShowsVisibility: Int = View.GONE,
                                val searchQuery: String = "",
                                val landingEvent: UIEvent<LandingEvent>? = null)

    sealed class LandingAction {
        object MovieClicked : LandingAction()
        object TelevisionClicked : LandingAction()
        data class Search(val searchQuery: String) : LandingAction()
        data class NavigateToDetails(val id: Int, val url: String, val title: String?) : LandingAction()
    }

    sealed class LandingEvent {
        data class ShowDetails(val id: Int,
                               val posterUrl: String,
                               val title: String,
                               val searchType: SearchType) : LandingEvent()
    }

    sealed class Resource<T>(open var data: T?) {
        data class Loading<T>(override var data: T?) : Resource<T>(data)
        data class Failure<T>(override var data: T?, val error: Throwable) : Resource<T>(data)
        data class Success<T>(override var data: T?) : Resource<T>(data)
        data class Stale<T>(override var data: T?) : Resource<T>(data)
    }
}