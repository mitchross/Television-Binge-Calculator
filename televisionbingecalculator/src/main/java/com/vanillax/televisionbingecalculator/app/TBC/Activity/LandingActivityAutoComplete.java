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
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryMasterAPI;
import com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse.ShowQueryMasterResponse;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TVBCLogger.EmptyResponse;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TVBCLogger.SearchTerm;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TVBCLoggerAPI;
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

/**
 * Created by mitchross on 11/29/15.
 */
public class LandingActivityAutoComplete extends BaseActivity implements ShowRecyclerAdapter.OnShowClickListener
{

	ShowRecyclerAdapter showRecyclerAdapter;
	List<ShowQueryMasterResponse> myShows;



	@Inject
	ShowQueryMasterAPI showQueryMasterAPI;

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

		initRxTextView();

		if ( myShows == null )
		{
			defaultText.setVisibility( View.VISIBLE );
			tvIcon.setVisibility( View.VISIBLE );
		}


	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	private void initRxTextView()
	{
		Observable<EditText> searchTextObservable = ViewObservable.text(searchField);
		searchTextObservable.debounce( 500, TimeUnit.MILLISECONDS )
				.map( search_field -> search_field.getText().toString() )
				.flatMap( searchTerm -> {
					Observable<List<ShowQueryMasterResponse>> showQueryResponse = null;

					showQueryResponse = showQueryMasterAPI.autoCompleteQuery( searchTerm  );

					Answers.getInstance().logCustom( new CustomEvent( "Search Query" ).putCustomAttribute( "Search Term", searchTerm ) );

					return showQueryResponse;
				})
				.observeOn( AndroidSchedulers.mainThread() )
				.subscribe( showQueryResponse ->
				{
					refreshListView( showQueryResponse );

				},Throwable::printStackTrace);


	}

	@Override
	public void onShowClicked( int showPosition )
	{


		ShowQueryMasterResponse selectedShow;
		selectedShow = myShows.get( showPosition );

		//Just do some logging
		tvbcLoggerAPI.postSearchTerm( new SearchTerm( selectedShow.getShowTitle() ), new Callback<EmptyResponse>()
		{
			@Override
			public void success( EmptyResponse emptyResponse, Response response )
			{
				showManager.setShow( selectedShow );

				Intent intent = new Intent( LandingActivityAutoComplete.this, ShowDetailsActivity.class );
				startActivity( intent );
			}

			@Override
			public void failure( RetrofitError error )
			{
				showManager.setShow( selectedShow );

				Intent intent = new Intent( LandingActivityAutoComplete.this, ShowDetailsActivity.class );
				startActivity( intent );
			}
		} );


	}

	protected void refreshListView( List<ShowQueryMasterResponse> showQueryMasterResponses )
	{
		progressBar.setVisibility( View.GONE );

		if ( showQueryMasterResponses != null )
		{
			defaultText.setVisibility( View.GONE );
			tvIcon.setVisibility( View.GONE );
		}

		myShows = showQueryMasterResponses;

		ArrayList<String> showTitles = new ArrayList<String>();
		ArrayList<String> showPosters = new ArrayList<String>();


		for ( ShowQueryMasterResponse show : showQueryMasterResponses )
		{
			showTitles.add( show.title );
			showPosters.add( CalculatorUtils.getShowPosterThumbnail( show.images.posterUrl, false ) );

		}

		showRecyclerAdapter = new ShowRecyclerAdapter( showTitles, showPosters, R.layout.spinnerrow, getApplicationContext(), LandingActivityAutoComplete.this );

		listView.setAdapter( showRecyclerAdapter );

	}

}
