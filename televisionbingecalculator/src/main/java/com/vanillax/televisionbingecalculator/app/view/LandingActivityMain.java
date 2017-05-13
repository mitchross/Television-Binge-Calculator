package com.vanillax.televisionbingecalculator.app.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.actions.SearchIntents;
import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TV.ShowPosterListing;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TV.TVQueryResponse;
import com.vanillax.televisionbingecalculator.app.TBC.TelevisionBingeCalculator;
import com.vanillax.televisionbingecalculator.app.TBC.adapters.ShowsAdapter;
import com.vanillax.televisionbingecalculator.app.TBC.adapters.SpacesItemDecoration;
import com.vanillax.televisionbingecalculator.app.databinding.ActivityMainMaterialBinding;
import com.vanillax.televisionbingecalculator.app.viewmodel.LandingActivityViewModel;

import java.util.List;

import io.fabric.sdk.android.Fabric;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;


public class LandingActivityMain extends AppCompatActivity implements LandingActivityViewModel.LandingActivityViewCallback
{

	ActivityMainMaterialBinding binding;
	ShowsAdapter showsAdapter = new ShowsAdapter(  );
	List<ShowPosterListing> shows;
	SpacesItemDecoration decoration;
	LandingActivityViewModel landingActivityViewModel;
	SearchType selectedSearchType = SearchType.TV;

	boolean searchInProgress;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		Fabric.with( this, new Crashlytics() );

		landingActivityViewModel = new LandingActivityViewModel();

		binding = DataBindingUtil.setContentView( this,R.layout.activity_main_material );
		binding.setView( this );

		TelevisionBingeCalculator.inject( this );
		binding.listView.setLayoutManager( new GridLayoutManager( this , 3 ));
		decoration = new SpacesItemDecoration( 3 , 35 , false );
		binding.listView.addItemDecoration( decoration );
		binding.listView.setAdapter( showsAdapter );

		showsAdapter.setListener( this );

		if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP )
		{
			setupWindowAnimations();
		}

		setUpEditTextListener();

		handleIntent( getIntent() );

	}

	@Override
	protected void onStart()
	{
		super.onStart();
		landingActivityViewModel.onViewAttached( this );
	}

	@Override
	protected void onDestroy()
	{

		showsAdapter.setListener( null );
		landingActivityViewModel.onViewDetached();
		super.onDestroy();
	}

	private void handleIntent( Intent intent )
	{
		init();

		String intentAction = intent.getAction();

		if ( SearchIntents.ACTION_SEARCH.equals( intentAction ) )
		{
			String query = intent.getStringExtra( SearchManager.QUERY );
			binding.searchField.setText( query );
		}
	}

	private void setUpEditTextListener()
	{
		binding.searchField.addTextChangedListener( new TextWatcher()
		{
			@Override
			public void beforeTextChanged( CharSequence s, int start, int count, int after )
			{

			}

			@Override
			public void onTextChanged( CharSequence s, int start, int before, int count )
			{
				if ( count == 0 )
				{
					hideListView();
				}
			}

			@Override
			public void afterTextChanged( Editable s )
			{

			}
		} );
	}

	public void onMovieClick()
	{
		binding.movieSelector.setVisibility( View.VISIBLE );
		binding.televisionSelector.setVisibility( View.GONE );
		selectedSearchType = SearchType.MOVIE;
		if ( binding.searchField.getText() !=null )
		{
			searchShow( binding.searchField.getText().toString(), selectedSearchType );
		}

	}

	public void onTelevisionClick()
	{
		binding.movieSelector.setVisibility( View.GONE );
		binding.televisionSelector.setVisibility( View.VISIBLE );
		selectedSearchType = SearchType.TV;
		if (binding.searchField.getText() !=null )
		{
			searchShow( binding.searchField.getText().toString(), selectedSearchType );
		}

	}


	@TargetApi( Build.VERSION_CODES.LOLLIPOP )
	private void setupWindowAnimations()
	{
		Transition fade = TransitionInflater.from( this ).inflateTransition( R.transition.activity_fade );
		getWindow().setEnterTransition( fade );
	}


	@Override
	protected void onResume()
	{
		super.onResume();

		if ( shows == null )
		{
			binding.defaultListviewText.setVisibility( View.VISIBLE );
			binding.tvIcon.setVisibility( View.VISIBLE );
		}

		landingActivityViewModel.onViewResumed();
		init();

	}

	private void init()
	{

		binding.searchField.setOnEditorActionListener( ( v, actionId, event ) -> {
			if (actionId == EditorInfo.IME_ACTION_SEARCH  ||
					actionId == EditorInfo.IME_ACTION_DONE ||
					actionId == EditorInfo.IME_ACTION_GO ||
					event.getKeyCode() == KeyEvent.KEYCODE_ENTER ) {


				searchShow( v.getText().toString(), selectedSearchType);

				InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService( Activity.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow( this.getCurrentFocus().getWindowToken(), 0);

				return true;
			}
			return false;
		} );

	}

	private void searchShow( String query, SearchType searchType )
	{
		landingActivityViewModel.searchShow( query , searchType );
	}

	private void hideListView()
	{
		binding.listView.setVisibility( View.GONE );
		binding.defaultListviewText.setVisibility( View.GONE );
		binding.tvIcon.setVisibility( View.GONE );
		binding.resultsFound.setText( "Results Found: 0" );
	}

	private void updateListView( TVQueryResponse tvQueryResponse )
	{
		binding.defaultListviewText.setVisibility( View.GONE );
		binding.tvIcon.setVisibility( View.GONE );

		binding.listView.setVisibility( View.VISIBLE );

		shows = tvQueryResponse.showPosterListings;

		showsAdapter.setShowsViewModelItems( shows );



	}


	@Override
	protected void onPause()
	{
		super.onPause();
	}


	private void navigateToDetails( int id, String posterUrl,String title )
	{
		searchInProgress = false;

		Intent intent = new Intent( LandingActivityMain.this, ShowDetailsActivity.class );
		intent.putExtra( "tvshow_id", id );
		intent.putExtra( "tvshow_thumbnail", posterUrl );
		intent.putExtra( "title",title );
		intent.putExtra( "show_type", selectedSearchType );
		intent.setFlags( FLAG_ACTIVITY_CLEAR_TOP );
		startActivity( intent );
	}




	@Override
	public void updateShowList( TVQueryResponse tvQueryResponse )
	{
		updateListView( tvQueryResponse );
	}

	@Override
	public void onItemTouch( int id, String url, String title )
	{
		if ( !searchInProgress )
		{

			searchInProgress = true;


			navigateToDetails( id, url,title );
			landingActivityViewModel.logShow( title );




		}
	}

	public enum SearchType
	{
		TV,
		MOVIE
	}
}
