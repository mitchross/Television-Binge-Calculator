package com.vanillax.televisionbingecalculator.app.kotlin.viewmodels

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vanillax.televisionbingecalculator.app.kotlin.enum.SearchType
import com.vanillax.televisionbingecalculator.app.kotlin.network.TVBCLoggerService
import com.vanillax.televisionbingecalculator.app.kotlin.network.TheMovieDBService
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.QueryResponse
import com.vanillax.televisionbingecalculator.app.serverapi.TVBCLogger.SearchTerm
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer


/**
 * Created by mitchross on 4/14/18.
 */
//TODO add tests around livedata and this viewmodel
class LandingActivityViewModel(
    theMovieDBService: TheMovieDBService,
    tvbcLoggerService: TVBCLoggerService
) : ViewModel(),DefaultLifecycleObserver {

    private val _queryResponse = MutableLiveData<QueryResponse>()
    val queryResponse: LiveData<QueryResponse>
        get() = _queryResponse


    private var disposable: Disposable? = null
    private val service = theMovieDBService
    private val tvbcLoggerService = tvbcLoggerService
    var isMovie = ObservableField<Boolean>(false)
    var searchType: SearchType = SearchType.TV
    var searchQuery: String = ""

    fun onDisconnect() {
        disposable?.dispose()
    }

    fun onGetSearchShow(query: String?) {
        if (!query.isNullOrEmpty()) {
            searchShow(query)
        } else {
            Log.d(this.javaClass.simpleName, "Please Enter a Search Term")
        }
    }

    private fun searchShow(query: String) {

        //Hold on to it for later
        searchQuery = query

        if (searchType.equals(SearchType.TV)) {
            disposable = service.queryTV(query)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        result.searchType = searchType
                        _queryResponse.value = result
                    },
                    { error -> Log.d(this.javaClass.simpleName,error?.message)}

                )
        } else {
            disposable = service.queryMovie(query)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        result.searchType = searchType
                        _queryResponse.value = result
                    },
                    { error -> Log.d(this.javaClass.simpleName,error?.message)}

                )
        }

    }

    fun onMovieClick() {

        isMovie.set(true)

        searchType = SearchType.MOVIE
        when {
            !searchQuery.isNullOrEmpty() -> searchShow(searchQuery)
        }

    }

    fun onTelevisionClick() {

        isMovie.set(false)
        searchType = SearchType.TV
        when {
            !searchQuery.isNullOrEmpty() -> searchShow(searchQuery)
        }


    }

    fun logShow(title: String?) {
        if (title != null) {
            val titleDetails: String

            when (searchType) {
                SearchType.MOVIE -> titleDetails = "Movie: $title"
                SearchType.TV -> titleDetails = "TV: $title"
            }

            disposable = tvbcLoggerService.postSearchTerm(SearchTerm(titleDetails))
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(Consumer { Log.d(this.javaClass.simpleName, "Post search term call completed") })

        }
    }


}