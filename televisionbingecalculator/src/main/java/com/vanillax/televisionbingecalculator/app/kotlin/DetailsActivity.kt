package com.vanillax.televisionbingecalculator.app.kotlin

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import com.vanillax.televisionbingecalculator.app.R
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.vanillax.televisionbingecalculator.app.ServerAPI.movie.Scoring
import com.vanillax.televisionbingecalculator.app.TBC.adapters.CastListRecyclerAdapter
import com.vanillax.televisionbingecalculator.app.TBC.adapters.StreamingSourceRecyclerAdapter
import com.vanillax.televisionbingecalculator.app.databinding.ActivityShowDetails2Binding
import com.vanillax.televisionbingecalculator.app.databinding.ActivityShowDetailsBinding
import com.vanillax.televisionbingecalculator.app.kotlin.enum.SearchType
import com.vanillax.televisionbingecalculator.app.kotlin.network.JustWatchAPIService
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.JustWatchResponse
import com.vanillax.televisionbingecalculator.app.kotlin.viewmodels.DetailsItemViewModel
import com.vanillax.televisionbingecalculator.app.kotlin.viewmodels.DetailsViewModel
import java.util.*
import android.text.method.ScrollingMovementMethod




class DetailsActivity: AppCompatActivity(), DetailsViewModel.DetailsViewModelInterface, AdapterView.OnItemSelectedListener {



    private lateinit var viewModel:DetailsViewModel
    private lateinit var binding: ActivityShowDetails2Binding

    internal var streamingSourceRecyclerAdapter = StreamingSourceRecyclerAdapter()
    internal var castListRecyclerAdapter = CastListRecyclerAdapter()

    internal var showId: Int = 0
    protected var showTitle: String = ""
    protected var thumbnailUrl: String = ""
    private var seasonSelected: Int = 0
    private lateinit var selectedSearchType: SearchType


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding and View Model initilization
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_details2)
        viewModel = DetailsViewModel(com.vanillax.televisionbingecalculator.app.kotlin.network.TheMovieDBService.create(this), JustWatchAPIService.create(this))
        binding.`object` = viewModel
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

       // binding.episodeDescription.setMovementMethod(ScrollingMovementMethod())

        binding.episodeDescription.movementMethod = ScrollingMovementMethod()


    }

    override fun onResume() {
        super.onResume()

        //Lifecycle get showData
        viewModel.getAllShowDetailsData(showId,selectedSearchType,showTitle )
    }

    override fun onPause() {
        super.onPause()
        viewModel.onDisconnect()
    }


    override fun onStop() {
        super.onStop()
        viewModel.onDisconnect()
    }



    private fun matchTitleAndGetData(justWatchResponse: JustWatchResponse) {
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

    private fun populateRecyclerViews( detailsItemViewModel: DetailsItemViewModel)
    {
         matchTitleAndGetData( detailsItemViewModel.justWatchResponse)
        initSpinners(detailsItemViewModel.seasonCount!!)
        castListRecyclerAdapter.setCastList(detailsItemViewModel.castResponse)

    }


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

