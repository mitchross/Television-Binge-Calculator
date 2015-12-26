package com.vanillax.televisionbingecalculator.app.TBC.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import io.fabric.sdk.android.Fabric;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Observable;
import rx.android.observables.ViewObservable;
import rx.android.schedulers.AndroidSchedulers;


public class LandingActivityMain extends BaseActivity implements ShowRecyclerAdapter.OnShowClickListener
{


	ShowRecyclerAdapter showRecyclerAdapter;
	List<TVQueryResponse.Result> shows;



	@Inject
	TheMovieDbAPI theMovieDbAPI;

	@Inject
	TVBCLoggerAPI tvbcLoggerAPI;

	@Inject
	ShowManager showManager;


	@InjectView( R.id.search_field )
	EditText searchField;

	@InjectView( R.id.progress_bar )
	SmoothProgressBar progressBar;

	@InjectView( R.id.default_listview_text )
	TextView defaultText;

	@InjectView( R.id.tv_icon )
	ImageView tvIcon;



	@InjectView( R.id.list_view )
	RecyclerView listView;


	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		Fabric.with( this, new Crashlytics() );
		setContentView( R.layout.activity_main_material );
		TelevisionBingeCalculator.inject( this );
		ButterKnife.inject( this );

		listView.setLayoutManager( new LinearLayoutManager( this ) );
		listView.setItemAnimator( new DefaultItemAnimator() );


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

		initRxTextView();

	}

	private void initRxTextView()
	{
		Observable<EditText> searchTextObservable = ViewObservable.text(searchField);
		searchTextObservable.debounce( 500, TimeUnit.MILLISECONDS )
				.map( search_field ->  search_field.getText().toString()  )
				.flatMap( searchTerm -> {
					Observable<TVQueryResponse> tvQueryResponseObservable = null;
					tvQueryResponseObservable = theMovieDbAPI.queryShow( searchTerm );
					return tvQueryResponseObservable;

				})
				.observeOn( AndroidSchedulers.mainThread() )
				.retry()
				.subscribe( tvQueryResponseObservable -> {
					//do something
					updateListView( tvQueryResponseObservable );
				});

	}

	private void updateListView( TVQueryResponse tvQueryResponse )
	{
		progressBar.setVisibility( View.GONE );

		if ( tvQueryResponse != null )
		{
			defaultText.setVisibility( View.GONE );
			tvIcon.setVisibility( View.GONE );
		}

		shows = tvQueryResponse.results;

		ArrayList<String> showTitles = new ArrayList<String>();
		ArrayList<String> showPosters = new ArrayList<String>();


		for ( TVQueryResponse.Result result : tvQueryResponse.results )
		{
			showTitles.add( result.original_name );
			showPosters.add( CalculatorUtils.getShowPosterThumbnail( result.posterPath, false ) );

		}

		showRecyclerAdapter = new ShowRecyclerAdapter( showTitles, showPosters, R.layout.spinnerrow, getApplicationContext(), LandingActivityMain.this );

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

		int id = shows.get( showPosition ).id;

		tvbcLoggerAPI.postSearchTerm( new SearchTerm( shows.get( showPosition ).original_name ), new Callback<EmptyResponse>()
		{
			@Override
			public void success( EmptyResponse emptyResponse, Response response )
			{
				Intent intent = new Intent( LandingActivityMain.this, ShowDetailsActivity.class );
				intent.putExtra( "tvshow_id", id );
				startActivity( intent );
			}

			@Override
			public void failure( RetrofitError error )
			{
				Intent intent = new Intent( LandingActivityMain.this, ShowDetailsActivity.class );
				intent.putExtra( "tvshow_id", id );
				startActivity( intent );
			}
		} );

	}



}
