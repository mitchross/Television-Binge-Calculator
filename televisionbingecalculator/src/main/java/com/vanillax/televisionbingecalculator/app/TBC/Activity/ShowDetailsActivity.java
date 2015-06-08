package com.vanillax.televisionbingecalculator.app.TBC.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse.ShowQueryMasterResponse;
import com.vanillax.televisionbingecalculator.app.TBC.BaseActivity;
import com.vanillax.televisionbingecalculator.app.TBC.ShowManager;
import com.vanillax.televisionbingecalculator.app.TBC.TelevisionBingeCalculator;
import com.vanillax.televisionbingecalculator.app.TBC.Utils.CalculatorUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ShowDetailsActivity extends BaseActivity implements Spinner.OnItemSelectedListener
{

	@Inject
	ShowManager showManager;


	@InjectView(R.id.poster_image)
	ImageView posterImage;

	@InjectView(R.id.episode_total)
	TextView episdoeCountTextView;

	@InjectView( R.id.episode_length )
	TextView episodeRunTime;

	@InjectView(R.id.binge_time)
	TextView bingTimeText;

	@InjectView( R.id.season_spinner)
	Spinner seasonSpinner;

	ShowQueryMasterResponse show;

	protected String numberSeasons;
	public String episodeCount;
	public int runtime;
	protected String bingeTime;
	protected String imageUrl;
	protected String title;




	@OnClick( R.id.fine_tune_link )
	 protected void showFineTune()
	{
		final Dialog dialog = new Dialog( this );
		dialog.setContentView( R.layout.settings_popup );


		Button dialogButton = (Button) dialog.findViewById( R.id.settings_done );
		final EditText openingCreditsTime = (EditText) dialog.findViewById( R.id.opening_credits_edittext );
		final EditText closingCreditsTime = (EditText) dialog.findViewById( R.id.closing_credits_edittext );
		final CheckBox hasCommercialsCheckBox = (CheckBox) dialog.findViewById( R.id.commercial_checkbox );


		dialogButton.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				int openCreditTime = openingCreditsTime.getText().toString().isEmpty()
									 ? 0
									 : Integer.parseInt( openingCreditsTime.getText().toString() );
				int closingCreditTime = closingCreditsTime.getText().toString().isEmpty()
										? 0
										: Integer.parseInt( closingCreditsTime.getText().toString() );
				boolean hasCommercials = hasCommercialsCheckBox.isChecked();

				String fineTuned = CalculatorUtils.calcFineTuneTime( ShowDetailsActivity.this,
						Integer.parseInt( episodeCount ),
						runtime,
						openCreditTime,
						closingCreditTime,
						hasCommercials );
				bingTimeText.setText( fineTuned );
				dialog.dismiss();

			}
		} );

		dialog.show();

	}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
		ButterKnife.inject( this );
		TelevisionBingeCalculator.inject( this );

		show = showManager.getShow();
		setUpView();

    }

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
    protected int getLayoutResource() {
        return  R.layout.activity_show_details_material_card_2;
    }

    @Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent( intent );
		setIntent( intent );
	}

	private void setUpView()
	{
		initSpinners();

		imageUrl = show.getShowDetailImageUrl();
		title = show.getShowTitle();

		Glide.with( getApplicationContext() )
				.load( imageUrl )
				.into( posterImage );

		getSupportActionBar().setTitle( title );

	}

	private void initSpinners()
	{
		int seasons = Integer.parseInt( show.getNumberOfSeasons() );

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

	private void initViewsForTotalBingeMode()
	{
		episodeCount = String.valueOf( show.getEpisodeCount() );
		runtime = show.getRunTime();
		bingeTime = CalculatorUtils.getTotalBingeTime( this, show );
		imageUrl = show.getShowDetailImageUrl();
		title = show.getShowTitle();


		episodeRunTime.setText( "" + runtime );
		episdoeCountTextView.setText( episodeCount );
		bingTimeText.setText( bingeTime );
	}

	private void initViewsForSpecificSeason( int seasonNumber )
	{
		episodeCount = String.valueOf( show.getNumberOfEpisodesForSeason( seasonNumber ) );
		runtime = show.getRunTime();
		bingeTime = CalculatorUtils.calcSpecificSeason( this, show, seasonNumber );
		imageUrl = show.getShowDetailImageUrl();
		title = show.getShowTitle();


		episodeRunTime.setText( "" + runtime );
		episdoeCountTextView.setText( episodeCount );
		bingTimeText.setText( bingeTime );
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
