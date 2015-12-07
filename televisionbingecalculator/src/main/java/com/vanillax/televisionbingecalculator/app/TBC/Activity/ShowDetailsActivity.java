package com.vanillax.televisionbingecalculator.app.TBC.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vanillax.televisionbingecalculator.app.R;
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
import butterknife.OnCheckedChanged;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ShowDetailsActivity extends BaseActivity implements SeasonsRecyclerAdapter.OnShowClickListener
{

	@Inject
	TheMovieDbAPI theMovieDbAPI;


	@InjectView(R.id.poster_image)
	ImageView posterImage;

	@InjectView(R.id.episode_total)
	TextView episdoeCountTextView;

	@InjectView( R.id.episode_length )
	TextView episodeRunTime;

	@InjectView(R.id.binge_time)
	TextView bingTimeText;

	@InjectView( R.id.seasons_recycler_view )
	RecyclerView seasonsRecyclerView;

	@InjectView( R.id.select_all_checkbox )
	CheckBox selectedAllCheckbox;

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




//	@OnClick( R.id.fine_tune_link )
//	 protected void showFineTune()
//	{
//		final Dialog dialog = new Dialog( this );
//		dialog.setContentView( R.layout.settings_popup );
//
//
//		Button dialogButton = (Button) dialog.findViewById( R.id.settings_done );
//		final EditText openingCreditsTime = (EditText) dialog.findViewById( R.id.opening_credits_edittext );
//		final EditText closingCreditsTime = (EditText) dialog.findViewById( R.id.closing_credits_edittext );
//		final CheckBox hasCommercialsCheckBox = (CheckBox) dialog.findViewById( R.id.commercial_checkbox );
//
//
//		dialogButton.setOnClickListener( v -> {
//			int openCreditTime = openingCreditsTime.getText().toString().isEmpty()
//								 ? 0
//								 : Integer.parseInt( openingCreditsTime.getText().toString() );
//			int closingCreditTime = closingCreditsTime.getText().toString().isEmpty()
//									? 0
//									: Integer.parseInt( closingCreditsTime.getText().toString() );
//			boolean hasCommercials = hasCommercialsCheckBox.isChecked();
//
//			String fineTuned = CalculatorUtils.calcFineTuneTime( ShowDetailsActivity.this,
//					Integer.parseInt( episodeCount ),
//					runtime,
//					openCreditTime,
//					closingCreditTime,
//					hasCommercials );
//			bingTimeText.setText( fineTuned );
//			dialog.dismiss();
//
//		} );
//
//		dialog.show();
//
//	}

	@OnCheckedChanged( R.id.select_all_checkbox )
	protected void onCheck(boolean checked)
	{
		if (checked)
		{
			initViewsForTotalBingeMode();
		}
		else
		{
			initViewsForSpecificSeason( 1 );
		}
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
		ButterKnife.inject( this );
		TelevisionBingeCalculator.inject( this );
		linearLayoutManager = new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, false );
		seasonsRecyclerView.setLayoutManager( linearLayoutManager );
		seasonsRecyclerView.setAdapter( seasonsRecyclerAdapter );


		showId = getIntent().getIntExtra( "tvshow_id" , 0 );


    }

	@Override
	protected int getLayoutResource()
	{
		return R.layout.activity_show_details_material_card_3;

	}

	@Override
	protected void onResume()
	{
		super.onResume();

		theMovieDbAPI.queryShowDetails( String.valueOf( showId ), new Callback<TVShowByIdResponse>()
		{
			@Override
			public void success( TVShowByIdResponse tvShowByIdResponse, Response response )
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(ShowDetailsActivity.this);
				builder.setMessage("TV has incomplete data. Sorry about that.")
						.setCancelable(false)
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								finish();
							}
						});
				AlertDialog alert = builder.create();


				alert.show();
				ShowDetailsActivity.this.tvShowByIdResponse = tvShowByIdResponse;
				setUpView();
			}

			@Override
			public void failure( RetrofitError error )
			{

			}
		} );


	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}




	private void setUpView()
	{
		initSeasonsRecyclerView();

		imageUrl = tvShowByIdResponse.imageUrl;
		title = tvShowByIdResponse.title;
		episodeDescription.setText( tvShowByIdResponse.episodeDescription );

		Glide.with( getApplicationContext() )
				.load( CalculatorUtils.getShowPosterThumbnail( imageUrl ) )
				.into( posterImage );

		getSupportActionBar().setTitle( title );

	}

	private void initSeasonsRecyclerView()
	{
		int totalSeasons = tvShowByIdResponse.numberOfSeasons();

		ArrayList<Integer> seasons = new ArrayList<>(  );


		for ( int i = 1; i < totalSeasons + 1; i++)
		{
			seasons.add( i);
		}


		seasonsRecyclerAdapter = new SeasonsRecyclerAdapter( seasons, R.layout.seasons_adapter_row, this , this );
		seasonsRecyclerView.setAdapter( seasonsRecyclerAdapter );
		initViewsForTotalBingeMode();
	}

	private void initViewsForTotalBingeMode()
	{
		episodeCount = String.valueOf( tvShowByIdResponse.getEpisodeCount() );
		runtime = tvShowByIdResponse.getRunTimeAverage();
		bingeTime = CalculatorUtils.getTotalBingeTime( this, tvShowByIdResponse );
		imageUrl = tvShowByIdResponse.imageUrl;
		title = tvShowByIdResponse.title;


		episodeRunTime.setText( "" + runtime );
		episdoeCountTextView.setText( episodeCount );
		bingTimeText.setText( bingeTime );
	}

	private void initViewsForSpecificSeason( int seasonNumber )
	{
		episodeCount = String.valueOf( tvShowByIdResponse.getNumberOfEpisodesForSeason( seasonNumber ) );
		runtime = tvShowByIdResponse.getRunTimeAverage();
		bingeTime = CalculatorUtils.calcSpecificSeason( this, tvShowByIdResponse, seasonNumber );
		imageUrl = tvShowByIdResponse.seasons.get( seasonNumber ).posterPath;
		title = tvShowByIdResponse.title;


		episodeRunTime.setText( "" + runtime );
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
