package com.vanillax.televisionbingecalculator.app.kotlin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.vanillax.televisionbingecalculator.app.kotlin.network.JustWatchAPIService
import com.vanillax.televisionbingecalculator.app.kotlin.network.TheMovieDBService
import com.vanillax.televisionbingecalculator.app.udf.UIEvent
import com.vanillax.televisionbingecalculator.app.viewmodel.SeasonNumberViewModelItem

class DetailsViewModel2 (theMovieDBService: TheMovieDBService,
                         justWatchAPIService: JustWatchAPIService) : ViewModel()
{
    private val _detailsViewState = MediatorLiveData<DetailsViewState>()
            .also { it.value = DetailsViewState() }

    val detailsViewState: LiveData<DetailsViewState> = _detailsViewState

    private val uiEvent = UIEvent<DetailsEvent>()


    fun onAction(action: DetailsAction) {

        when (action) {

            is DetailsAction.SeasonNumberClicked -> {

            }
        }

    }




    data class DetailsViewState( val isLoading: Boolean = true,
                                 val posterUrl: String = "",
                                 val thumbnailUrl: String ="",
                                 val showTitle: String ="",
                                 val year: String ="",
                                 val category: String="",
                                 val totalBingeTimeString: String="",
                                 val showDescription:String ="",
                                 val episodeCountString:String ="",
                                 val isMovie:Boolean = false,
                                 val runtimeString: String = "",
                                 val seasonCount: ArrayList<SeasonNumberViewModelItem>
                                 )

    sealed class DetailsAction {
        //todo is this right?
        data class SeasonNumberClicked(val seasonNumber: Int): DetailsAction()
    }

    sealed class DetailsEvent {
        //todo what goes here???
    }

}