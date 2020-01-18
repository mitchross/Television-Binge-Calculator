package com.vanillax.televisionbingecalculator.app.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.vanillax.televisionbingecalculator.app.R
import com.vanillax.televisionbingecalculator.app.databinding.ActivityShowDetails2Binding
import com.vanillax.televisionbingecalculator.app.kotlin.enum.SearchType
import com.vanillax.televisionbingecalculator.app.kotlin.network.JustWatchAPIService
import com.vanillax.televisionbingecalculator.app.kotlin.network.TheMovieDBService
import com.vanillax.televisionbingecalculator.app.kotlin.utils.getViewModel
import com.vanillax.televisionbingecalculator.app.kotlin.viewmodels.DetailsViewModel2
import com.vanillax.televisionbingecalculator.app.tbc.adapters.*

class DetailsActivity2 : AppCompatActivity() {

    private lateinit var seasonNumberRecyclerAdapter: SeasonNumberRecyclerAdapter2
    private lateinit var viewModel: DetailsViewModel2
    private lateinit var binding: ActivityShowDetails2Binding
    private lateinit var streamingSourceRecyclerAdapter: StreamingSourceRecyclerAdapter2
    private lateinit var castListRecyclerAdapter: CastListRecyclerAdapter2

    internal var showId: Int = 0
    protected var showTitle: String = ""
    protected var thumbnailUrl: String = ""
    private lateinit var selectedSearchType: SearchType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_details2)

        //Intent data from previous activitiy
        showId = intent.getIntExtra("tvshow_id", 0)
        thumbnailUrl = intent.getStringExtra("tvshow_thumbnail").orEmpty()
        showTitle = intent.getStringExtra("title").orEmpty()
        selectedSearchType = intent.getSerializableExtra("show_type") as SearchType

        seasonNumberRecyclerAdapter = SeasonNumberRecyclerAdapter2().also {
            binding.seasonsNumberList.adapter = it
            binding.seasonsNumberList.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }

        streamingSourceRecyclerAdapter = StreamingSourceRecyclerAdapter2().also {
            binding.steamingLogoRecyclerView.adapter = it
            binding.steamingLogoRecyclerView.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }

        castListRecyclerAdapter = CastListRecyclerAdapter2().also {
            binding.castRecyclerView.adapter = it
            binding.castRecyclerView.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }

        viewModel = getViewModel {
            DetailsViewModel2(TheMovieDBService.create(this),
                    JustWatchAPIService.create(this)
            )
        }

        viewModel.detailsViewState.observe(this, Observer {
            binding.viewState = it

            seasonNumberRecyclerAdapter.submitList(it.seasonNumberItems)
            castListRecyclerAdapter.submitList(it.castList)
            streamingSourceRecyclerAdapter.submitList(it.streamingSources)

            it.detailsUIEvent?.value?.let { event ->

                when (event) {

                    is DetailsViewModel2.DetailsUIEvent.FakeEvent -> {
                        // no-op
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.onAction(DetailsViewModel2.DetailsAction.FetchAllShowDetails(showId, selectedSearchType, showTitle))
    }
}