package com.vanillax.televisionbingecalculator.app.TBC.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryMasterAPI;
import com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse.ShowQueryMasterResponse;
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
import roboguice.util.Ln;
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


//	@Optional
//	@OnClick( R.id.search_button )
//	protected void searchShow()
//	{
//		final String showToSearch = searchField.getText().toString();
//
//		tvbcLoggerAPI.postSearchTerm( new SearchTerm( showToSearch ), new Callback<EmptyResponse>()
//		{
//			@Override
//			public void success( EmptyResponse emptyResponse, Response response )
//			{
//				showQueryMasterAPI.queryShow( showToSearch, true, new ShowQueryMasterResponseCallback() );
//				Answers.getInstance().logCustom( new CustomEvent( "Search Query" ).putCustomAttribute( "Search Term", showToSearch ) );
//
//			}
//
//			@Override
//			public void failure( RetrofitError error )
//			{
//				showQueryMasterAPI.queryShow( showToSearch, true, new ShowQueryMasterResponseCallback() );
//
//			}
//		} );
//	}

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

		Observable<EditText> searchTextObservable = ViewObservable.text(searchField);
		searchTextObservable.debounce( 500, TimeUnit.MILLISECONDS )
				.map( search_field -> search_field.getText().toString() )
				.flatMap( searchTerm -> {
					Observable<List<ShowQueryMasterResponse>> showQueryResponse = null;

					showQueryResponse = showQueryMasterAPI.autoCompleteQuery( searchTerm  );
					return showQueryResponse;
				})
				.observeOn( AndroidSchedulers.mainThread() )
				.subscribe( showQueryResponse ->
				{
					refreshListView( showQueryResponse );

				},Throwable::printStackTrace);




//		// set up the action listener for the text field
//		searchField.setOnEditorActionListener( new EditText.OnEditorActionListener()
//		{
//			@Override
//			public boolean onEditorAction( final TextView searchTextView, int actionID, KeyEvent event )
//			{
//				progressBar.setVisibility( View.VISIBLE );
//				Ln.d( "onEditorAction... searchTerm: %s", searchTextView.getText().toString() );
//				final String showToSearch = searchField.getText().toString();
//
//				tvbcLoggerAPI.postSearchTerm( new SearchTerm( showToSearch ), new Callback<EmptyResponse>()
//				{
//					@Override
//					public void success( EmptyResponse emptyResponse, Response response )
//					{
//						showQueryMasterAPI.queryShow( showToSearch, true, new ShowQueryMasterResponseCallback() );
//						Answers.getInstance().logCustom( new CustomEvent( "Search Query" ).putCustomAttribute( "Search Term", showToSearch ) );
//
//
//					}
//
//					@Override
//					public void failure( RetrofitError error )
//					{
//						showQueryMasterAPI.queryShow( showToSearch, true, new ShowQueryMasterResponseCallback() );
//
//					}
//				} );
//
//				return true;
//			}
//
//		} );
//
//		listView.setLayoutManager( new LinearLayoutManager( this ) );
//		listView.setItemAnimator( new DefaultItemAnimator() );


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

	@Override
	public void onShowClicked( int showPosition )
	{
		ShowQueryMasterResponse selectedShow;
		selectedShow = myShows.get( showPosition );
		showManager.setShow( selectedShow );

		Intent intent = new Intent( this, ShowDetailsActivity.class );
		startActivity( intent );


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
			showPosters.add( CalculatorUtils.getShowPosterThumbnail( show.images.posterUrl ) );

		}

		showRecyclerAdapter = new ShowRecyclerAdapter( showTitles, showPosters, R.layout.spinnerrow, getApplicationContext(), LandingActivityAutoComplete.this );

		listView.setAdapter( showRecyclerAdapter );

	}

	public class ShowQueryMasterResponseCallback implements Callback<List<ShowQueryMasterResponse>>
	{

		@Override
		public void success( List<ShowQueryMasterResponse> showQueryMasterResponses, Response response )
		{
			refreshListView( showQueryMasterResponses );

		}

		@Override
		public void failure( RetrofitError retrofitError )
		{
			progressBar.setVisibility( View.GONE );
			Ln.d( "fail" );
		}
	}
}
