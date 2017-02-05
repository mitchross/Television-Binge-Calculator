package com.vanillax.televisionbingecalculator.app.TBC.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.actions.SearchIntents;
import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TV.TVQueryResponse;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TVBCLogger.EmptyResponse;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TVBCLogger.SearchTerm;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TVBCLoggerAPI;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TheMovieDbAPI;
import com.vanillax.televisionbingecalculator.app.TBC.BaseActivity;
import com.vanillax.televisionbingecalculator.app.TBC.ShowManager;
import com.vanillax.televisionbingecalculator.app.TBC.TelevisionBingeCalculator;
import com.vanillax.televisionbingecalculator.app.TBC.Utils.CalculatorUtils;
import com.vanillax.televisionbingecalculator.app.TBC.adapters.ShowRecyclerAdapter;
import com.vanillax.televisionbingecalculator.app.TBC.adapters.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import io.fabric.sdk.android.Fabric;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;


public class LandingActivityMain extends BaseActivity implements ShowRecyclerAdapter.OnShowClickListener
{


	ShowRecyclerAdapter showRecyclerAdapter;
	List<TVQueryResponse.Result> shows;
	SpacesItemDecoration decoration;

	boolean searchInProgress;

	@Inject
	TheMovieDbAPI theMovieDbAPI;

	@Inject
	TVBCLoggerAPI tvbcLoggerAPI;

	@Inject
	ShowManager showManager;

	@InjectView( R.id.default_listview_text )
	TextView defaultText;

	@InjectView( R.id.tv_icon )
	ImageView tvIcon;


	@InjectView( R.id.search_field )
	EditText searchField;

	@InjectView( R.id.progress_bar )
	SmoothProgressBar progressBar;


	@InjectView( R.id.results_found )
	TextView resultsFound;

	@InjectView( R.id.list_view )
	RecyclerView listView;



	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		Fabric.with( this, new Crashlytics() );
		TelevisionBingeCalculator.inject( this );
		ButterKnife.inject( this );

		listView.setLayoutManager( new GridLayoutManager( this , 3 ) );
		decoration = new SpacesItemDecoration( 3 , 35 , false );
		listView.addItemDecoration( decoration );


		if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP )
		{
			setupWindowAnimations();
		}

		setUpEditTextListener();

		handleIntent( getIntent() );

	}


	private void handleIntent( Intent intent )
	{
		init();

		String intentAction = intent.getAction();

		if ( SearchIntents.ACTION_SEARCH.equals( intentAction ) )
		{
			String query = intent.getStringExtra( SearchManager.QUERY );
			searchField.setText( query );
		}
	}

	private void setUpEditTextListener()
	{
		searchField.addTextChangedListener( new TextWatcher()
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

	@TargetApi( Build.VERSION_CODES.LOLLIPOP )
	private void setupWindowAnimations()
	{
		Transition fade = TransitionInflater.from( this ).inflateTransition( R.transition.activity_fade );
		getWindow().setEnterTransition( fade );
	}

	@Override
	protected int getLayoutResource()
	{
		return R.layout.activity_main_material;
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		if ( shows == null )
		{
			defaultText.setVisibility( View.VISIBLE );
			tvIcon.setVisibility( View.VISIBLE );
		}

		init();

	}

	private void init()
	{

		searchField.setOnEditorActionListener( ( v, actionId, event ) -> {
			if (actionId == EditorInfo.IME_ACTION_SEARCH  || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || event.getKeyCode() == KeyEvent.KEYCODE_ENTER ) {


				theMovieDbAPI.queryShow( String.valueOf( v.getText().toString() ) )
						.subscribeOn( Schedulers.newThread() )
						.observeOn( AndroidSchedulers.mainThread() )
						.subscribe( new Subscriber<TVQueryResponse>()
						{
							@Override
							public void onCompleted()
							{

							}

							@Override
							public void onError( Throwable e )
							{
								finish();
							}

							@Override
							public void onNext( TVQueryResponse response )
							{
								updateListView( response );
							}
						} );


				InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService( Activity.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow( this.getCurrentFocus().getWindowToken(), 0);

				return true;
			}
			return false;
		} );

	}

	private void hideListView()
	{
		listView.setVisibility( View.GONE );
		defaultText.setVisibility( View.GONE );
		tvIcon.setVisibility( View.GONE );
		resultsFound.setText( "Results Found: 0" );
	}

	private void updateListView( TVQueryResponse tvQueryResponse )
	{
		progressBar.setVisibility( View.GONE );
		defaultText.setVisibility( View.GONE );
		tvIcon.setVisibility( View.GONE );

		listView.setVisibility( View.VISIBLE );

		shows = tvQueryResponse.results;

		ArrayList<String> showTitles = new ArrayList<String>();
		ArrayList<String> showPosters = new ArrayList<String>();


		resultsFound.setText( String.format( "Results Found: %d", tvQueryResponse.results.size() ) );
		for ( TVQueryResponse.Result result : tvQueryResponse.results )
		{
			showTitles.add( result.original_name );
			showPosters.add( CalculatorUtils.getShowPosterThumbnail( result.posterPath, false ) );

		}

		showRecyclerAdapter = new ShowRecyclerAdapter( showTitles, showPosters, R.layout.grid_cell, getApplicationContext(), LandingActivityMain.this );

		listView.setAdapter( showRecyclerAdapter );

	}


	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	public void onShowClicked( int showPosition )
	{
		if ( !searchInProgress )
		{

			searchInProgress = true;

			int id = shows.get( showPosition ).id;
			String path = shows.get( showPosition ).posterPath;
			String posterUrl = CalculatorUtils.getShowPosterThumbnail( path, false );


			navigateToDetails( id, posterUrl );

			tvbcLoggerAPI.postSearchTerm( new SearchTerm( shows.get( showPosition ).original_name ) )
					.subscribeOn( Schedulers.newThread() )
					.observeOn( AndroidSchedulers.mainThread() )
					.subscribe( new Subscriber<EmptyResponse>()
					{
						@Override
						public void onCompleted()
						{

						}

						@Override
						public void onError( Throwable e )
						{

						}

						@Override
						public void onNext( EmptyResponse emptyResponse )
						{

						}
					} );


		}
	}

	private void navigateToDetails( int id, String posterUrl )
	{
		searchInProgress = false;

		Intent intent = new Intent( LandingActivityMain.this, ShowDetailsActivity.class );
		intent.putExtra( "tvshow_id", id );
		intent.putExtra( "tvshow_thumbnail", posterUrl );
		intent.setFlags( FLAG_ACTIVITY_CLEAR_TOP );
		startActivity( intent );
	}



}
