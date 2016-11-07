package com.vanillax.televisionbingecalculator.app.TBC.Activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SwitchCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.ServerAPI.GuideBoxApi;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TV.TVShowByIdResponse;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TheMovieDbAPI;
import com.vanillax.televisionbingecalculator.app.TBC.BaseActivity;
import com.vanillax.televisionbingecalculator.app.TBC.TelevisionBingeCalculator;
import com.vanillax.televisionbingecalculator.app.TBC.Utils.CalculatorUtils;
import com.vanillax.televisionbingecalculator.app.TBC.adapters.SeasonsRecyclerAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ShowDetailsActivity extends BaseActivity implements SeasonsRecyclerAdapter.OnShowClickListener
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

//	@InjectView( R.id.seasons_recycler_view )
//	RecyclerView seasonsRecyclerView;

	@InjectView( R.id.switch_toggle )
	SwitchCompat selectedAllCheckbox;

	@InjectView( R.id.episode_description )
	TextView episodeDescription;

	LinearLayoutManager linearLayoutManager;
	SeasonsRecyclerAdapter seasonsRecyclerAdapter;

	int showId;
	TVShowByIdResponse tvShowByIdResponse;

	protected String numberSeasons;
	public String episodeCount;
	public int runtime;
	protected String bingeTime;
	protected String imageUrl;
	protected String title;
	protected String thumbnailUrl;


//	@OnCheckedChanged( R.id.select_all_checkbox )
//	protected void onCheck(boolean checked)
//	{
//		if (checked)
//		{
//			initViewsForTotalBingeMode();
//		}
//		else
//		{
//			initViewsForSpecificSeason( 0 );
//		}
//	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
		ButterKnife.inject( this );
		TelevisionBingeCalculator.inject( this );
		linearLayoutManager = new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, false );
//		seasonsRecyclerView.setLayoutManager( linearLayoutManager );
//		seasonsRecyclerView.setAdapter( seasonsRecyclerAdapter );


		showId = getIntent().getIntExtra( "tvshow_id" , 0 );
		thumbnailUrl = getIntent().getStringExtra( "tvshow_thumbnail" );


    }

	@Override
	protected int getLayoutResource()
	{
		return R.layout.activity_show_details_2;

	}

	@Override
	protected void onResume()
	{
		super.onResume();

		//getStreamingSources();

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
						finish();
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
		super.onPause();
	}

//
//	private void getStreamingSources()
//	{
//		guideBoxApi.translateTheMovieDBID( String.valueOf( showId ) )
//				.subscribeOn( Schedulers.newThread())
//				.observeOn( AndroidSchedulers.mainThread() )
//				.flatMap( new Func1<GuideBoxShowTranslatorResponse, Observable<?>>()
//				{
//					@Override
//					public Observable<?> call( GuideBoxShowTranslatorResponse guideBoxShowTranslatorResponse )
//					{
//						return guideBoxApi.getAvailableContent( String.valueOf( guideBoxShowTranslatorResponse.id ));
//					}
//
//				})
//				.subscribe( new Action1<Object>()
//				{
//				} );
//
//
//
//		////
//
//
//
//
//	}


	private void setUpView()
	{
		initSeasonsRecyclerView();

		imageUrl = tvShowByIdResponse.imageUrl;
		title = tvShowByIdResponse.title;
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

	private void initSeasonsRecyclerView()
	{
		int totalSeasons = tvShowByIdResponse.numberOfSeasons;

		ArrayList<Integer> seasons = new ArrayList<>(  );


		for ( int i = 1; i < totalSeasons + 1 ; i++)
		{
			seasons.add( i);
		}


		seasonsRecyclerAdapter = new SeasonsRecyclerAdapter( seasons, R.layout.seasons_adapter_row, this , this );
		//seasonsRecyclerView.setAdapter( seasonsRecyclerAdapter );
		initViewsForTotalBingeMode();
	}

	private void initViewsForTotalBingeMode()
	{
		episodeCount = String.valueOf( tvShowByIdResponse.getEpisodeCount() );
		runtime = tvShowByIdResponse.getRunTimeAverage();
		bingeTime = CalculatorUtils.getTotalBingeTime( this, tvShowByIdResponse );
		imageUrl = tvShowByIdResponse.imageUrl;
		title = tvShowByIdResponse.title;


		//episodeRunTime.setText( "" + runtime );
		episdoeCountTextView.setText( episodeCount );
		bingTimeText.setText( bingeTime );
		selectedAllCheckbox.setChecked( true );

		seasonsRecyclerAdapter.setSelected( -1 );
		seasonsRecyclerAdapter.notifyDataSetChanged();
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
	public void onSeasonsClicked( int showPosition )
	{
		initViewsForSpecificSeason( showPosition );
		seasonsRecyclerAdapter.notifyDataSetChanged();
	}
}
