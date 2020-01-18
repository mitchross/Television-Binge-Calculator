package com.vanillax.televisionbingecalculator.app.kotlin

import android.annotation.TargetApi
import android.app.Activity
import android.app.SearchManager
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionInflater
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.actions.SearchIntents
import com.vanillax.televisionbingecalculator.app.R
import com.vanillax.televisionbingecalculator.app.kotlin.adapters.ShowsAdapter
import com.vanillax.televisionbingecalculator.app.kotlin.enum.SearchType
import com.vanillax.televisionbingecalculator.app.kotlin.network.TVBCLoggerService
import com.vanillax.televisionbingecalculator.app.kotlin.network.TheMovieDBService
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.QueryResponse
import com.vanillax.televisionbingecalculator.app.kotlin.utils.getViewModel
import com.vanillax.televisionbingecalculator.app.kotlin.viewmodels.LandingActivityViewModel
import com.vanillax.televisionbingecalculator.app.kotlin.viewmodels.PosterThumbnailViewModel
import com.vanillax.televisionbingecalculator.app.tbc.adapters.SpacesItemDecoration

/**
 * Created by mitchross on 4/14/18.
 */

class LandingActivity : AppCompatActivity() {

    private lateinit var viewModel: LandingActivityViewModel
    private lateinit var binding: com.vanillax.televisionbingecalculator.app.databinding.ActivityMainMaterialBinding
    private lateinit var showsAdapter: ShowsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding and View Model initilization
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_material)
        viewModel = getViewModel {
            LandingActivityViewModel(
                    TheMovieDBService.create(this),
                    TVBCLoggerService.create()
            )
        }

        viewModel.landingViewState.observe(this, Observer {
            binding.viewState = it

            showsAdapter.setShowsViewModelItems(it.showPosterListings)

            it.landingEvent?.value?.let { event ->

                when (event) {

                    is LandingActivityViewModel.LandingEvent.ShowDetails -> {
                        val intent = Intent(this, DetailsActivity2::class.java)
                        intent.putExtra("tvshow_id", event.id)
                        intent.putExtra("tvshow_thumbnail", event.posterUrl)
                        intent.putExtra("title", event.title)
                        intent.putExtra("show_type", event.searchType)
                        intent.flags = FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                    }
                }
            }
        })

        binding.tvTab.setOnClickListener {
            viewModel.onAction(LandingActivityViewModel.LandingAction.TelevisionClicked)
        }

        binding.movieTab.setOnClickListener {
            viewModel.onAction(LandingActivityViewModel.LandingAction.MovieClicked)
        }

        binding.listView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 3)
        binding.listView.addItemDecoration(SpacesItemDecoration(3, 35, false))

        showsAdapter = ShowsAdapter { id, url, title ->
            viewModel.onAction(LandingActivityViewModel.LandingAction.NavigateToDetails(id, url, title))
        }

        binding.listView.adapter = showsAdapter

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupWindowAnimations()
        }

        handleIntent(intent)
    }

    private fun searchShowBasedOnkeyboardInput() {

        binding.searchField.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_GO ||
                    event.keyCode == KeyEvent.KEYCODE_ENTER
            ) {

                val query = v.text.toString()
                viewModel.onAction(LandingActivityViewModel.LandingAction.Search(query))

                val inputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)

                true
            } else {
                false
            }
        }
    }

    private fun handleIntent(intent: Intent) {

        searchShowBasedOnkeyboardInput()

        val intentAction = intent.action

        if (SearchIntents.ACTION_SEARCH == intentAction) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            binding.searchField.setText(query)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupWindowAnimations() {
        val fade = TransitionInflater.from(this).inflateTransition(R.transition.activity_fade)
        window.enterTransition = fade
    }
}