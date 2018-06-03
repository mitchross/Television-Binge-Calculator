package com.vanillax.televisionbingecalculator.app.Kotlin

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.vanillax.televisionbingecalculator.app.Kotlin.enum.SearchType
import com.vanillax.televisionbingecalculator.app.Kotlin.network.JustWatchAPIService
import com.vanillax.televisionbingecalculator.app.Kotlin.network.response.JustWatchResponse
import com.vanillax.televisionbingecalculator.app.Kotlin.viewmodels.DetailsItemViewModel
import com.vanillax.televisionbingecalculator.app.Kotlin.viewmodels.DetailsViewModel
import com.vanillax.televisionbingecalculator.app.R
import com.vanillax.televisionbingecalculator.app.ServerAPI.movie.Scoring
import com.vanillax.televisionbingecalculator.app.TBC.adapters.CastListRecyclerAdapter
import com.vanillax.televisionbingecalculator.app.TBC.adapters.StreamingSourceRecyclerAdapter
import com.vanillax.televisionbingecalculator.app.databinding.ActivityShowDetailsBinding
import java.util.*

class DetailsActivity: AppCompatActivity(), DetailsViewModel.DetailsViewModelInterface, AdapterView.OnItemSelectedListener {



    private lateinit var viewModel:DetailsViewModel
    private lateinit var binding: ActivityShowDetailsBinding

    internal var streamingSourceRecyclerAdapter = StreamingSourceRecyclerAdapter()
    internal var castListRecyclerAdapter = CastListRecyclerAdapter()

    internal var showId: Int = 0
    protected var showTitle: String = ""
    protected var thumbnailUrl: String = ""
    private var seasonSelected: Int = 0
    private var selectedSearchType: SearchType = SearchType.TV


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding and View Model initilization
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_details)
        viewModel = DetailsViewModel(com.vanillax.televisionbingecalculator.app.Kotlin.network.TheMovieDBService.create(), JustWatchAPIService.create())
        binding.item = viewModel
        viewModel.setListener(this)

        //Intent data from previous activitiy
        showId = intent.getIntExtra("tvshow_id", 0)
        thumbnailUrl = intent.getStringExtra("tvshow_thumbnail")
        showTitle = intent.getStringExtra("title")
        selectedSearchType = intent.getSerializableExtra("show_type") as SearchType

        //Set Up and pass data to our view model
        viewModel.setShowMetaData(showId,showTitle, thumbnailUrl, selectedSearchType)

        setUpViews()
    }

    private fun setUpViews()
    {

        //Toolbar init
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = ""
        }

        //TODO change
        binding.steamingLogoRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.steamingLogoRecyclerView.adapter = streamingSourceRecyclerAdapter


        binding.castRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.castRecyclerView.adapter = castListRecyclerAdapter




    }

    override fun onResume() {
        super.onResume()

        //Lifecycle get showData
        viewModel.getAllShowDetailsData(showId,selectedSearchType,showTitle )
    }


    override fun onStop() {
        super.onStop()
        viewModel.onDisconnect()
    }



    private fun matchTitleAndGetData(justWatchResponse: JustWatchResponse ) {
        if (justWatchResponse.items != null || justWatchResponse.items!!.size != 0) {
            for (justWatchSearchItem in justWatchResponse.items!!) {
                if (showTitle.contains(justWatchSearchItem.title!!)) {
                    if (justWatchSearchItem.scoringList != null) {
                        initScoring(justWatchSearchItem.scoringList!!)
                    }
                    streamingSourceRecyclerAdapter.setStreamingSourceViewModelItems(justWatchSearchItem.offers!!)
                }
                break
            }
        }
    }



    private fun initScoring(scoringList: List<Scoring>) {
        val metaCritic = "metacritic:score:"
        val imdbScore = "imdb:score"

        //default state
        binding.metacriticScore.visibility = View.GONE
        binding.metacriticScore.visibility = View.GONE


        for (s in scoringList) {
            if (s.providerType == metaCritic) {
                binding.metacriticScore.visibility = View.VISIBLE
                binding.metacriticScore.text = "Metacritic Score: " + s.value!!
            }
            if (s.providerType == imdbScore) {
                binding.imdbScore.visibility = View.VISIBLE
                binding.imdbScore.text = "IMDB Score: " + s.value!!
            }

        }


    }

    private fun initSpinners(seasonCount:Int) {


        val items = ArrayList<String>()

        val allSeasons = "All ($seasonCount)"
        items.add(allSeasons)

        for (i in 1 until seasonCount + 1) {
            items.add(i.toString())
        }

        val adapter = ArrayAdapter(this, R.layout.season_spinner_row, items)
        binding.seasonSpinner.adapter = adapter
        binding.seasonSpinner.setOnItemSelectedListener(this)
    }

    //

    private fun populateRecyclerViews( detailsItemViewModel: DetailsItemViewModel )
    {
         matchTitleAndGetData( detailsItemViewModel.justWatchResponse)
        initSpinners(detailsItemViewModel.seasonCount!!)

    }

    //View Setup

//    private fun populateView( detailsItemViewModel: DetailsItemViewModel )
//    {
//        binding.episodeRuntime.text = runtime.toString() + " minutes"
//        binding.episodeTotal.text = episodeCount
//
//        binding.bingeTime.text = bingeTime
//        binding.switchToggle.isChecked = true
//    }


//    private fun initShowData(tvShowByIdResponse: TVShowByIdResponse) {
//
//        val seasons = tvShowByIdResponse.numberOfSeasons
//        val items = ArrayList<String>()
//        val allSeasons = "All ($seasons)"
//        items.add(allSeasons)
//
//        for (i in 1 until seasons + 1) {
//            items.add(i.toString())
//        }
//
//        val adapter = ArrayAdapter(this, R.layout.season_spinner_row, items)
//        binding.seasonSpinner.adapter = adapter
//
//
//
//        binding.seasonSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//
//            }
//
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//
//                if (position == 0) {
//                    if (selectedSearchType != LandingActivityMain.SearchType.MOVIE) {
//                        viewModel.getAllShowDetailsData(showId,selectedSearchType,showTitle )
//                    }
//                } else {
//                    seasonSelected = position
//                    initViewsForSpecificSeason(seasonSelected, tvShowByIdResponse)
//                }
//
//            }
//
//        }
 //   }






//
//    private fun initViewsForSpecificSeason(seasonNumber: Int, tvShowByIdResponse: TVShowByIdResponse) {
//        if (tvShowByIdResponse != null) {
//            episodeCount = tvShowByIdResponse.getNumberOfEpisodesForSeason(seasonNumber).toString() + " Episodes"
//            runtime = tvShowByIdResponse.getRunTimeAverage()
//            bingeTime = CalculatorUtils.calcSpecificSeason(this, tvShowByIdResponse, seasonNumber)
//
//            imageUrl = if (seasonNumber == 0)
//                tvShowByIdResponse.seasons?.get(seasonNumber).posterPath.toString()
//            else
//                tvShowByIdResponse.seasons?.get(seasonNumber - 1).posterPath.toString()
//            //showTitle = tvShowByIdResponse.title;
//
//            binding.episodeRuntime.text = runtime.toString() + " minutes"
//            binding.episodeTotal.text = episodeCount
//            binding.bingeTime.text = bingeTime
//            binding.switchToggle.isChecked = false
//        }
//    }



    //Callbacks

//    override fun onFetchDetails(tvShowByIdResponse: TVShowByIdResponse) {
//        if (tvShowByIdResponse.seasons.size == 0) {
//            val builder = AlertDialog.Builder(this@DetailsActivity)
//            builder.setMessage("TV show has incomplete data. Sorry about that.")
//                    .setCancelable(false)
//                    .setPositiveButton("OK") { dialog, id -> finish() }
//            val alert = builder.create()
//            alert.show()
//        }
//
//        initShowData(tvShowByIdResponse)
//    }

override fun onFetchAllDetails(detailsItemViewModel: DetailsItemViewModel) {
    Log.d("test", "got data")
    populateRecyclerViews( detailsItemViewModel )
}

override fun error(error: String?) {
    Log.d("test", "error")

}

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {


            viewModel.selectSeason( p2 )

    }



}

