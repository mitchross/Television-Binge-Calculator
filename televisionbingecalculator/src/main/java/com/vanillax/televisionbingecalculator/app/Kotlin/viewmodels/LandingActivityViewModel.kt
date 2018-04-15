package com.vanillax.televisionbingecalculator.app.Kotlin.viewmodels

import com.vanillax.televisionbingecalculator.app.Kotlin.QueryResponse
import com.vanillax.televisionbingecalculator.app.Kotlin.network.TheMovieDBService
import com.vanillax.televisionbingecalculator.app.Kotlin.enum.SearchType
import io.reactivex.disposables.Disposable

/**
 * Created by mitchross on 4/14/18.
 */
class  LandingActivityViewModel(theMovieDBService: TheMovieDBService) {

    interface LandingActivityViewModelInterface {
        fun updateShowList( queryResponse: QueryResponse)
        fun onTouch( id: Int, url: String, title: String, searchType: String )
        fun error( error: String?)
    }

    private var disposable: Disposable? = null
    private var listener: LandingActivityViewModelInterface? = null
    private val service = theMovieDBService


    fun setListener( listener: LandingActivityViewModelInterface) {
        this.listener = listener
    }

    fun onDisconnect() {
        disposable?.dispose()
    }

    fun onGetSearchShow( query: String?, searchType: SearchType)
    {
        if ( query !=null && !query.isNullOrEmpty() )
        {
            searchShow( query, searchType)
        }
        else {
            listener?.error("Please Enter a Search Term")
        }
    }

    private fun searchShow ( query: String, searchType: SearchType) {
        disposable = service.queryTV( query)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn( io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->  listener?.updateShowList( result )},
                        { error -> listener?.error(error?.message)}

                )

    }
}