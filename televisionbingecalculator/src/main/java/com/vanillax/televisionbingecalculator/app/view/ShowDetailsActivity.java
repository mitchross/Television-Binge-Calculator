package com.vanillax.televisionbingecalculator.app.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.ServerAPI.GuideBoxApi;
import com.vanillax.televisionbingecalculator.app.ServerAPI.GuideBoxResponse.GuideBoxAvailableContentResponse;
import com.vanillax.televisionbingecalculator.app.ServerAPI.JustWatchAPI;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TV.TVShowByIdResponse;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TheMovieDbAPI;
import com.vanillax.televisionbingecalculator.app.ServerAPI.movie.JustWatchSearch;
import com.vanillax.televisionbingecalculator.app.ServerAPI.movie.JustWatchSearchItem;
import com.vanillax.televisionbingecalculator.app.ServerAPI.movie.JustWatchShowResponse;
import com.vanillax.televisionbingecalculator.app.ServerAPI.movie.Offer;
import com.vanillax.televisionbingecalculator.app.TBC.TelevisionBingeCalculator;
import com.vanillax.televisionbingecalculator.app.TBC.Utils.CalculatorUtils;
import com.vanillax.televisionbingecalculator.app.TBC.adapters.StreamingSourceRecyclerAdapter;
import com.vanillax.televisionbingecalculator.app.databinding.ActivityShowDetailsBinding;
import com.vanillax.televisionbingecalculator.app.viewmodel.ShowDetailsViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import roboguice.util.Ln;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ShowDetailsActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener
{

	@Inject
	TheMovieDbAPI theMovieDbAPI;

	@Inject
	GuideBoxApi guideBoxApi;

	@Inject
	JustWatchAPI justWatchAPI;

	StreamingSourceRecyclerAdapter streamingSourceRecyclerAdapter = new StreamingSourceRecyclerAdapter();

	ShowDetailsViewModel showDetailsViewModel;
	ActivityShowDetailsBinding binding;

	int showId;
	TVShowByIdResponse tvShowByIdResponse;

	public String episodeCount;
	public int runtime;
	protected String bingeTime;
	protected String imageUrl;
	protected String showTitle;
	protected String thumbnailUrl;
	private SwitchCompat switchCompat;
	private boolean ignoreToggleListener = false;
	private int seasonSelected;
	private LandingActivityMain.SearchType selectedSearchType;




	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

		showDetailsViewModel = new ShowDetailsViewModel();

		binding = DataBindingUtil.setContentView( this, R.layout.activity_show_details );
		binding.setItem( this );




		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle( "" );
		}


		TelevisionBingeCalculator.inject( this );


		binding.steamingLogoRecyclerView.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, false ) );
		binding.steamingLogoRecyclerView.setAdapter( streamingSourceRecyclerAdapter );


		showId = getIntent().getIntExtra( "tvshow_id" , 0 );
		thumbnailUrl = getIntent().getStringExtra( "tvshow_thumbnail" );
		showTitle = getIntent().getStringExtra( "title" );
		selectedSearchType = (LandingActivityMain.SearchType) getIntent().getSerializableExtra( "show_type" );


    }

    private String getShowTitle()
	{
		return showTitle;
	}

	public void onChecked(boolean checked)
	{
		if (checked)
		{
			initViewsForTotalBingeMode();
		}
		else
		{
			initViewsForSpecificSeason( seasonSelected );
		}
	}



	@Override
	protected void onResume()
	{
		super.onResume();

		switch ( selectedSearchType )
		{
			case TV:
				fetchTV();
				break;
			default:
				fetchMovie();
				break;
		}




	}

	@Override
	protected void onPause()
	{
		//streamingSourceContainer.removeAllViews();
		super.onPause();
	}

	private void fetchTV()
	{
		theMovieDbAPI.queryTVDetails( String.valueOf( showId ) )
				.subscribeOn( Schedulers.newThread() )
				.observeOn( AndroidSchedulers.mainThread() )
				.subscribe( new Subscriber<TVShowByIdResponse>()
				{
					@Override
					public void onCompleted()
					{

					}

					@Override
					public void onError( Throwable e )
					{
						Log.e("error", e.toString());
					}

					@Override
					public void onNext( TVShowByIdResponse tvShowByIdResponse )
					{
						if (tvShowByIdResponse.seasons.size() == 0   )
						{
							AlertDialog.Builder builder = new AlertDialog.Builder( ShowDetailsActivity.this );
							builder.setMessage( "TV show has incomplete data. Sorry about that." )
									.setCancelable( false )
									.setPositiveButton( "OK", ( dialog, id ) -> {
										finish();
									} );
							AlertDialog alert = builder.create();
							alert.show();
						}


						ShowDetailsActivity.this.tvShowByIdResponse = tvShowByIdResponse;


						setUpTvView();
					}
				} );
	}

	private void fetchMovie()
	{
		theMovieDbAPI.queryMovieDetails( String.valueOf( showId ) )
				.subscribeOn( Schedulers.newThread() )
				.observeOn( AndroidSchedulers.mainThread() )
				.subscribe( new Subscriber<TVShowByIdResponse>()
				{
					@Override
					public void onCompleted()
					{

					}

					@Override
					public void onError( Throwable e )
					{
						Log.e("error", e.toString());
					}

					@Override
					public void onNext( TVShowByIdResponse tvShowByIdResponse )
					{
//						if (tvShowByIdResponse.seasons.size() == 0   )
//						{
//							AlertDialog.Builder builder = new AlertDialog.Builder( ShowDetailsActivity.this );
//							builder.setMessage( "TV show has incomplete data. Sorry about that." )
//									.setCancelable( false )
//									.setPositiveButton( "OK", ( dialog, id ) -> {
//										finish();
//									} );
//							AlertDialog alert = builder.create();
//							alert.show();
//						}


						ShowDetailsActivity.this.tvShowByIdResponse = tvShowByIdResponse;


						setUpMoviewView();
					}
				} );
	}




	private void initSpinners()
	{
		int seasons =  tvShowByIdResponse.numberOfSeasons;

		ArrayList<String> items = new ArrayList<>(  );

		String allSeasons = "All (" + seasons + ")";
		items.add( allSeasons );

		for ( int i = 1; i < seasons + 1; i++)
		{
			items.add( String.valueOf( i ) );
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.season_spinner_row, items);
		binding.seasonSpinner.setAdapter( adapter );
		binding.seasonSpinner.setOnItemSelectedListener( this );
	}



	private void getTVStreamingSources()
	{
		guideBoxApi.translateTheMovieDBID_TV( String.valueOf( showId ) )
			.subscribeOn( Schedulers.io() )
			.observeOn( AndroidSchedulers.mainThread() )
			.flatMap( guideBoxShowTranslatorResponse -> guideBoxApi.getAvailableContent_TV( String.valueOf( guideBoxShowTranslatorResponse.id ) )
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread()) )
				.subscribe( new Subscriber<GuideBoxAvailableContentResponse>()
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
					public void onNext( GuideBoxAvailableContentResponse guideBoxAvailableContentResponse )
					{
						Ln.d( guideBoxAvailableContentResponse);

						initSeasonsRecyclerView( guideBoxAvailableContentResponse.getStreamSources());
					}
				} );


						////


	}

	private void getMovieStreamingSources()
	{
		justWatchAPI.getMovieStreamingSources( new JustWatchSearch( showTitle ) )
				.subscribeOn( Schedulers.io() )
				.observeOn( AndroidSchedulers.mainThread() )
				.subscribe( new Subscriber<JustWatchShowResponse>()
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
					public void onNext( JustWatchShowResponse justWatchShowResponse )
					{
						matchTitleAndGetSources( justWatchShowResponse );
					}
				} );
	}

	private void matchTitleAndGetSources( JustWatchShowResponse justWatchShowResponse )
	{
		if ( justWatchShowResponse.items !=null || (justWatchShowResponse.items.size() != 0 ) )
		{
			for ( JustWatchSearchItem justWatchSearchItem  : justWatchShowResponse.items )
			{
				if ( getShowTitle().contains( justWatchSearchItem.title ))
				{
					initMovieStreamingSources( justWatchSearchItem.offers);
					Ln.d( justWatchSearchItem.title );
					break;
				}
			}

		}
	}



	private void setUpTvView()
	{
		initSpinners();

		imageUrl = tvShowByIdResponse.imageUrl;
		showTitle = tvShowByIdResponse.title == null
					? tvShowByIdResponse.movie_title
					: tvShowByIdResponse.title;

		binding.title.setText( showTitle );
		binding.year.setText( tvShowByIdResponse.getYear() );
		binding.categoryTitle.setText( tvShowByIdResponse.getCategory() );


		binding.episodeDescription.setText( tvShowByIdResponse.episodeDescription );

		Glide.with( getApplicationContext() )
				.load( CalculatorUtils.getShowPosterThumbnail( imageUrl, true ) )
				.error( getResources().getDrawable( R.drawable.tv_icon ) )
				.placeholder( getResources().getDrawable( R.drawable.tv_icon ) )
				.into( binding.posterImage );



		Glide.with( getApplicationContext() )
				.load( thumbnailUrl )
				.error( getResources().getDrawable( R.drawable.tv_icon ) )
				.placeholder( getResources().getDrawable( R.drawable.tv_icon ) )
				.into( binding.thumbnail );

		getTVStreamingSources();



	}

	private void setUpMoviewView()
	{
	//	initSpinners();

		binding.switchToggle.setVisibility( View.GONE );
		binding.seasonSpinner.setVisibility( View.GONE );
		binding.allSeasonsTitle.setVisibility( View.GONE );

		imageUrl = tvShowByIdResponse.imageUrl;
		showTitle = tvShowByIdResponse.title == null
					? tvShowByIdResponse.movie_title
					: tvShowByIdResponse.title;

		binding.title.setText( showTitle );
		binding.year.setText( tvShowByIdResponse.getMovieYear() );
		binding.categoryTitle.setText( tvShowByIdResponse.getCategory() );


		int runtime = Integer.parseInt( tvShowByIdResponse.getMovieRunTime() );
		binding.bingeTime.setText( CalculatorUtils.convertToDaysHoursMins( this, runtime  ) );



		binding.episodeDescription.setText( tvShowByIdResponse.episodeDescription );

		Glide.with( getApplicationContext() )
				.load( CalculatorUtils.getShowPosterThumbnail( imageUrl, true ) )
				.error( getResources().getDrawable( R.drawable.tv_icon ) )
				.placeholder( getResources().getDrawable( R.drawable.tv_icon ) )
				.into( binding.posterImage );



		Glide.with( getApplicationContext() )
				.load( thumbnailUrl )
				.error( getResources().getDrawable( R.drawable.tv_icon ) )
				.placeholder( getResources().getDrawable( R.drawable.tv_icon ) )
				.into( binding.thumbnail );

		getMovieStreamingSources();

	}


	private void initSeasonsRecyclerView( List<GuideBoxAvailableContentResponse.StreamSource> streamSourceList )
	{

		streamingSourceRecyclerAdapter.setTVStreamingSourceViewModelItems( streamSourceList );

	}

	private void initMovieStreamingSources(List<Offer> movieOffers)
	{
		streamingSourceRecyclerAdapter.setMovieStreamingSourceViewModelItems( movieOffers );
	}


	private void initViewsForTotalBingeMode()
	{
		if ( selectedSearchType != LandingActivityMain.SearchType.MOVIE )
		{
			episodeCount = String.valueOf( tvShowByIdResponse.getEpisodeCount() ) + " Episodes";
			runtime = tvShowByIdResponse.getRunTimeAverage();
			bingeTime = CalculatorUtils.getTotalBingeTime( this, tvShowByIdResponse );

			binding.episodeRuntime.setText(  runtime + " minutes");
			binding.episodeTotal.setText( episodeCount );
			binding.bingeTime.setText( bingeTime );
			binding.switchToggle.setChecked( true );
			imageUrl = tvShowByIdResponse.imageUrl;
		}


		//showTitle = tvShowByIdResponse.title;



	}

	private void initViewsForSpecificSeason( int seasonNumber )
	{
		episodeCount = String.valueOf( tvShowByIdResponse.getNumberOfEpisodesForSeason( seasonNumber ) ) + " Episodes";
		runtime = tvShowByIdResponse.getRunTimeAverage();
		bingeTime = CalculatorUtils.calcSpecificSeason( this, tvShowByIdResponse, seasonNumber );
		imageUrl = tvShowByIdResponse.seasons.get( (seasonNumber) ).posterPath;
		//showTitle = tvShowByIdResponse.title;

		binding.episodeRuntime.setText(  runtime + " minutes");
		binding.episodeTotal.setText( episodeCount );
		binding.bingeTime.setText( bingeTime );
		ignoreToggleListener = true;
		binding.switchToggle.setChecked(false);
	}



	@Override
	public void onItemSelected( AdapterView<?> parent, View view, int position, long id )
	{
		if ( position == 0 )
		{
			initViewsForTotalBingeMode();
		}
		else
		{
			seasonSelected = position;
			initViewsForSpecificSeason( seasonSelected );
		}
	}

	@Override
	public void onNothingSelected( AdapterView<?> parent )
	{

	}

}
