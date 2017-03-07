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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.vanillax.televisionbingecalculator.app.ServerAPI.TV.ShowPosterListing;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TV.TVQueryResponse;
import com.vanillax.televisionbingecalculator.app.TBC.Activity.ShowDetailsActivity;
import com.vanillax.televisionbingecalculator.app.TBC.adapters.ShowsAdapter;
import com.vanillax.televisionbingecalculator.app.TBC.adapters.SpacesItemDecoration;
import com.vanillax.televisionbingecalculator.app.databinding.ActivityMainMaterialBinding;
import com.vanillax.televisionbingecalculator.app.viewmodel.LandingActivityViewModel;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import io.fabric.sdk.android.Fabric;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;


public class LandingActivityMain extends AppCompatActivity implements LandingActivityViewModel.LandingActivityViewCallback
{

	ActivityMainMaterialBinding binding;
	ShowsAdapter showsAdapter = new ShowsAdapter(  );
	List<ShowPosterListing> shows;
	SpacesItemDecoration decoration;

	LandingActivityViewModel landingActivityViewModel;


	boolean searchInProgress;



	@Optional
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



		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}

		landingActivityViewModel = new LandingActivityViewModel();



		binding = DataBindingUtil.setContentView( this,R.layout.activity_main_material );

		ButterKnife.inject( this );

		binding.listView.setLayoutManager( new GridLayoutManager( this , 3 ));
		decoration = new SpacesItemDecoration( 3 , 35 , false );
		listView.addItemDecoration( decoration );
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

//	@Override
//	protected int getLayoutResource()
//	{
//		//return R.layout.activity_main_material;
//	}

	@Override
	protected void onResume()
	{
		super.onResume();

		if ( shows == null )
		{
			defaultText.setVisibility( View.VISIBLE );
			tvIcon.setVisibility( View.VISIBLE );
		}

		landingActivityViewModel.onViewResumed();
		init();

	}

	private void init()
	{

		searchField.setOnEditorActionListener( ( v, actionId, event ) -> {
			if (actionId == EditorInfo.IME_ACTION_SEARCH  ||
					actionId == EditorInfo.IME_ACTION_DONE ||
					actionId == EditorInfo.IME_ACTION_GO ||
					event.getKeyCode() == KeyEvent.KEYCODE_ENTER ) {


				searchShow( v.getText().toString() );



				InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService( Activity.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow( this.getCurrentFocus().getWindowToken(), 0);

				return true;
			}
			return false;
		} );

	}

	private void searchShow( String query)
	{
		landingActivityViewModel.searchShow( query );
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

		shows = tvQueryResponse.showPosterListings;

		showsAdapter.setShowsViewModelItems( shows );



	}


	@Override
	protected void onPause()
	{
		super.onPause();
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




	@Override
	public void updateShowList( TVQueryResponse tvQueryResponse )
	{
		updateListView( tvQueryResponse );
	}

	@Override
	public void onItemTouch( int id, String url )
	{
		if ( !searchInProgress )
		{

			searchInProgress = true;

//			int id = shows.get( showPosition ).id;
//			String path = shows.get( showPosition ).posterPath;
//			String posterUrl = CalculatorUtils.getShowPosterThumbnail( path, false );


			navigateToDetails( id, url );

//			tvbcLoggerAPI.postSearchTerm( new SearchTerm( shows.get( showPosition ).original_name ) )
//					.subscribeOn( Schedulers.newThread() )
//					.observeOn( AndroidSchedulers.mainThread() )
//					.subscribe( new Subscriber<EmptyResponse>()
//					{
//						@Override
//						public void onCompleted()
//						{
//
//						}
//
//						@Override
//						public void onError( Throwable e )
//						{
//
//						}
//
//						@Override
//						public void onNext( EmptyResponse emptyResponse )
//						{
//
//						}
//					} );


		}
	}
}
