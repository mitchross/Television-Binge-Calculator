package com.vanillax.televisionbingecalculator.app.TBC.Activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.ServerAPI.GuideBoxApi;
import com.vanillax.televisionbingecalculator.app.ServerAPI.GuideBoxResponse.GuideBoxAvailableContentResponse;
import com.vanillax.televisionbingecalculator.app.ServerAPI.GuideBoxResponse.GuideBoxShowTranslatorResponse;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TV.TVShowByIdResponse;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TheMovieDbAPI;
import com.vanillax.televisionbingecalculator.app.TBC.BaseActivity;
import com.vanillax.televisionbingecalculator.app.TBC.TelevisionBingeCalculator;
import com.vanillax.televisionbingecalculator.app.TBC.Utils.CalculatorUtils;
import com.vanillax.televisionbingecalculator.app.TBC.adapters.StreamingSourceRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import roboguice.util.Ln;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class ShowDetailsActivity extends BaseActivity implements Spinner.OnItemSelectedListener
{

	@Inject
	TheMovieDbAPI theMovieDbAPI;

	@Inject
	GuideBoxApi guideBoxApi;


	@InjectView(R.id.poster_image)
	ImageView posterImage;

	@InjectView(R.id.episode_total)
	TextView episdoeCountTextView;

//	@InjectView( R.id.episode_length )
//	TextView episodeRunTime;

	@InjectView( R.id.thumbnail )
	ImageView thumbnail;

	@InjectView(R.id.binge_time)
	TextView bingTimeText;

//	@InjectView( R.id.steaming_logo_recycler_view )
//	RecyclerView streamingLogoRecyclerView;

	@InjectView( R.id.switch_toggle )
	SwitchCompat selectedAllCheckbox;

	@InjectView( R.id.season_spinner )
	Spinner seasonSpinner;

	@InjectView( R.id.episode_description )
	TextView episodeDescription;

	@InjectView( R.id.title )
	TextView showtitle;

	@InjectView( R.id.category_title )
	TextView categoryTitle;

	@InjectView( R.id.year )
	TextView year;

	@InjectView( R.id.amazon )
	ImageView amazonImage;

	@InjectView( R.id.hulu )
	ImageView huluImage;

	@InjectView( R.id.netflix )
	ImageView netflixImage;

	@InjectView( R.id.vudu )
	ImageView vuduImage;

	@InjectView( R.id.hbo )
	ImageView hboImage;

	@InjectView( R.id.streaming_source_container)
	LinearLayout streamingSourceContainer;

	LinearLayoutManager linearLayoutManager;
	StreamingSourceRecyclerAdapter seasonsRecyclerAdapter;

	int showId;
	TVShowByIdResponse tvShowByIdResponse;

	protected String numberSeasons;
	public String episodeCount;
	public int runtime;
	protected String bingeTime;
	protected String imageUrl;
	protected String title;
	protected String thumbnailUrl;


	@OnCheckedChanged( R.id.switch_toggle )
	protected void onCheck(boolean checked)
	{
		if (checked)
		{
			initViewsForTotalBingeMode();
		}
		else
		{
			initViewsForSpecificSeason( 0 );
		}
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
		ButterKnife.inject( this );
		TelevisionBingeCalculator.inject( this );
		linearLayoutManager = new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, false );
	//	streamingLogoRecyclerView.setLayoutManager( linearLayoutManager );


		showId = getIntent().getIntExtra( "tvshow_id" , 0 );
		thumbnailUrl = getIntent().getStringExtra( "tvshow_thumbnail" );


    }

	@Override
	protected int getLayoutResource()
	{
		return R.layout.activity_show_details;

	}

	@Override
	protected void onResume()
	{
		super.onResume();

		getStreamingSources();

		theMovieDbAPI.queryShowDetails( String.valueOf( showId ) )
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
						if (tvShowByIdResponse.seasons.size() == 0 )
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


						setUpView();
					}
				} );

	}

	@Override
	protected void onPause()
	{
		streamingSourceContainer.removeAllViews();
		super.onPause();
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
		seasonSpinner.setAdapter( adapter );
		seasonSpinner.setOnItemSelectedListener( this );
	}



	private void getStreamingSources()
	{
		guideBoxApi.translateTheMovieDBID( String.valueOf( showId ) )
			.subscribeOn( Schedulers.io() )
			.observeOn( AndroidSchedulers.mainThread() )
			.flatMap( new Func1<GuideBoxShowTranslatorResponse, Observable<GuideBoxAvailableContentResponse>>()
						  {
							  @Override
							  public Observable<GuideBoxAvailableContentResponse> call( GuideBoxShowTranslatorResponse guideBoxShowTranslatorResponse )
							  {
								  return guideBoxApi.getAvailableContent( String.valueOf( guideBoxShowTranslatorResponse.id ) )
										  .subscribeOn(Schedulers.io())
										  .observeOn(AndroidSchedulers.mainThread());
							  }
						  })
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


	private void setUpView()
	{
		initSpinners();

		imageUrl = tvShowByIdResponse.imageUrl;
		title = tvShowByIdResponse.title;
		showtitle.setText( title );
		year.setText( tvShowByIdResponse.getYear() );
		categoryTitle.setText( tvShowByIdResponse.getCategory() );


		episodeDescription.setText( tvShowByIdResponse.episodeDescription );

		Glide.with( getApplicationContext() )
				.load( CalculatorUtils.getShowPosterThumbnail( imageUrl, true ) )
				.error( getResources().getDrawable( R.drawable.tv_icon ) )
				.placeholder( getResources().getDrawable( R.drawable.tv_icon ) )
				.into( posterImage );



		Glide.with( getApplicationContext() )
				.load( thumbnailUrl )
				.error( getResources().getDrawable( R.drawable.tv_icon ) )
				.placeholder( getResources().getDrawable( R.drawable.tv_icon ) )
				.into( thumbnail );

		getSupportActionBar().setTitle( title );

	}

	private void initSeasonsRecyclerView( List<GuideBoxAvailableContentResponse.StreamSource> streamSourceList )
	{

		for( GuideBoxAvailableContentResponse.StreamSource s : streamSourceList)
		{

			String streamingIconName = s.sourceDisplayName.toLowerCase();

			if( streamingIconName.contains( "netflix" ))
			{
				netflixImage.setVisibility( View.VISIBLE );
			}
			if ( streamingIconName.contains( "hulu" ))
			{
				huluImage.setVisibility( View.VISIBLE );

			}
			if ( streamingIconName.contains( "amazon" ))
			{
				amazonImage.setVisibility( View.VISIBLE );
			}
			if ( streamingIconName.contains( "vudu" ))
			{
				vuduImage.setVisibility( View.VISIBLE );
			}
			if ( streamingIconName.contains( "hbo" ))
			{
				hboImage.setVisibility( View.VISIBLE );
			}


		}

	}

	private void initViewsForTotalBingeMode()
	{
		episodeCount = String.valueOf( tvShowByIdResponse.getEpisodeCount() ) + " Episodes";
		runtime = tvShowByIdResponse.getRunTimeAverage();
		bingeTime = CalculatorUtils.getTotalBingeTime( this, tvShowByIdResponse );
		imageUrl = tvShowByIdResponse.imageUrl;
		title = tvShowByIdResponse.title;


		//episodeRunTime.setText( "" + runtime );
		episdoeCountTextView.setText( episodeCount );
		bingTimeText.setText( bingeTime );
		selectedAllCheckbox.setChecked( true );

	}

	private void initViewsForSpecificSeason( int seasonNumber )
	{
		episodeCount = String.valueOf( tvShowByIdResponse.getNumberOfEpisodesForSeason( seasonNumber ) );
		runtime = tvShowByIdResponse.getRunTimeAverage();
		bingeTime = CalculatorUtils.calcSpecificSeason( this, tvShowByIdResponse, seasonNumber );
		imageUrl = tvShowByIdResponse.seasons.get( seasonNumber ).posterPath;
		title = tvShowByIdResponse.title;


		//episodeRunTime.setText( "" + runtime );
		episdoeCountTextView.setText( episodeCount );
		bingTimeText.setText( bingeTime );

		seasonsRecyclerAdapter.setSelected( seasonNumber );
		selectedAllCheckbox.setChecked( false );
		seasonsRecyclerAdapter.notifyDataSetChanged();
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
			initViewsForSpecificSeason( position );
		}
	}

	@Override
	public void onNothingSelected( AdapterView<?> parent )
	{

	}

}
