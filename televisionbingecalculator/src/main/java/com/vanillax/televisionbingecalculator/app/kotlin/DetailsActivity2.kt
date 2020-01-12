package com.vanillax.televisionbingecalculator.app.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.vanillax.televisionbingecalculator.app.R
import com.vanillax.televisionbingecalculator.app.databinding.ActivityShowDetails2Binding
import com.vanillax.televisionbingecalculator.app.kotlin.enum.SearchType
import com.vanillax.televisionbingecalculator.app.kotlin.network.JustWatchAPIService
import com.vanillax.televisionbingecalculator.app.kotlin.network.TheMovieDBService
import com.vanillax.televisionbingecalculator.app.kotlin.utils.getViewModel
import com.vanillax.televisionbingecalculator.app.kotlin.viewmodels.DetailsViewModel2
import com.vanillax.televisionbingecalculator.app.tbc.adapters.SeasonNumberRecyclerAdapter




class DetailsActivity2 : AppCompatActivity() {

    private lateinit var seasonNumberRecyclerAdapter: SeasonNumberRecyclerAdapter
    private lateinit var viewModel: DetailsViewModel2
    private lateinit var binding: ActivityShowDetails2Binding

    internal var showId: Int = 0
    protected var showTitle: String = ""
    protected var thumbnailUrl: String = ""
    private lateinit var selectedSearchType: SearchType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Intent data from previous activitiy
        showId = intent.getIntExtra("tvshow_id", 0)
        thumbnailUrl = intent.getStringExtra("tvshow_thumbnail")
        showTitle = intent.getStringExtra("title")
        selectedSearchType = intent.getSerializableExtra("show_type") as SearchType

       binding = DataBindingUtil.setContentView(this, R.layout.activity_show_details2 )

        viewModel = getViewModel {
            DetailsViewModel2(TheMovieDBService.create(this),
                    JustWatchAPIService.create(this)
            )
        }

       viewModel.detailsViewState.observe(this, Observer {
           binding.viewState = it

           seasonNumberRecyclerAdapter.setSeasonList(it.seasonCount)


           it.detailsUIEvent?.value?.let { event ->

               when (event) {

                   is DetailsViewModel2.DetailsUIEvent.FakeEvent -> {
                       // no-op
                   }
               }
           }


       })

        seasonNumberRecyclerAdapter = SeasonNumberRecyclerAdapter { it: Int ->
            viewModel.onAction(DetailsViewModel2.DetailsAction.SeasonNumberClicked(it))
        }

        binding.seasonsNumberList.adapter = seasonNumberRecyclerAdapter

    }
}