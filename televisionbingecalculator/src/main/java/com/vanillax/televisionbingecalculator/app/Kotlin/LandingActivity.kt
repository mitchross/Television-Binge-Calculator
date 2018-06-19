package com.vanillax.televisionbingecalculator.app.Kotlin

import android.annotation.TargetApi
import android.app.Activity
import android.app.SearchManager
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionInflater
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.google.android.gms.actions.SearchIntents
import com.vanillax.televisionbingecalculator.app.Kotlin.adapters.ShowsAdapter
import com.vanillax.televisionbingecalculator.app.Kotlin.enum.SearchType
import com.vanillax.televisionbingecalculator.app.Kotlin.network.TVBCLoggerService
import com.vanillax.televisionbingecalculator.app.Kotlin.network.TheMovieDBService
import com.vanillax.televisionbingecalculator.app.Kotlin.network.response.QueryResponse
import com.vanillax.televisionbingecalculator.app.Kotlin.viewmodels.LandingActivityViewModel
import com.vanillax.televisionbingecalculator.app.R
import com.vanillax.televisionbingecalculator.app.TBC.adapters.SpacesItemDecoration
import com.vanillax.televisionbingecalculator.app.databinding.ActivityMainMaterialBinding

/**
 * Created by mitchross on 4/14/18.
 */

class LandingActivity : AppCompatActivity(), LandingActivityViewModel.LandingActivityViewModelInterface {


    private lateinit var viewModel:LandingActivityViewModel
    private lateinit var binding: ActivityMainMaterialBinding

    internal var showsAdapter = ShowsAdapter(this)
    internal lateinit var decoration: SpacesItemDecoration
    internal var selectedSearchType = SearchType.TV


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //Binding and View Model initilization
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_material)
        viewModel = LandingActivityViewModel(TheMovieDBService.create(this), TVBCLoggerService.create() )
        binding.view = viewModel
        viewModel.setListener(this)


        binding.listView.layoutManager = GridLayoutManager(this, 3)
        decoration = SpacesItemDecoration(3, 35, false)
        binding.listView.addItemDecoration(decoration)
        binding.listView.adapter = showsAdapter

        showsAdapter.setListener(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupWindowAnimations()
        }


        setUpEditTextListener()
        handleIntent(intent)


    }

    override fun onStop() {
        super.onStop()
        viewModel.onDisconnect()
    }


    private fun searchShowBasedOnkeyboardInput() {

        binding.searchField.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_GO ||
                    event.keyCode == KeyEvent.KEYCODE_ENTER) {


               val query = v.text.toString()

                viewModel.onGetSearchShow( query )


                val inputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)

                true
            }
            else {
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


    private fun setUpEditTextListener() {
        binding.searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count == 0) {
                    hideListView()
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    private fun hideListView() {
        binding.listView.visibility = View.GONE
        binding.defaultListviewText.visibility = View.VISIBLE
        binding.welcomeTitle.visibility = View.VISIBLE
        binding.resultsFound.text = "Results Found: 0"
    }

    private fun updateListView(queryResponse: QueryResponse) {
        binding.defaultListviewText.visibility = View.GONE
        binding.welcomeTitle.visibility = View.GONE


        binding.listView.visibility = View.VISIBLE


        showsAdapter.setShowsViewModelItems(queryResponse.showPosterListing)


    }

    private fun navigateToDetails(id: Int, posterUrl: String, title: String) {
        //searchInProgress = false

        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("tvshow_id", id)
        intent.putExtra("tvshow_thumbnail", posterUrl)
        intent.putExtra("title", title)
        intent.putExtra("show_type",selectedSearchType)
        intent.flags = FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }





    //Call backs


    override fun updateShowList(queryResponse: QueryResponse, searchType: SearchType) {

        selectedSearchType = searchType
        updateListView( queryResponse )
    }

    override fun onTouch(id: Int, url: String, title: String) {
       Log.d("success", "made it")
        navigateToDetails(id, url, title)
        viewModel.logShow(title )
    }

    override fun error(error: String?) {
        Log.d("error", error)
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupWindowAnimations() {
        val fade = TransitionInflater.from(this).inflateTransition(R.transition.activity_fade)
        window.enterTransition = fade
    }


}