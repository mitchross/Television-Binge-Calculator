package com.vanillax.televisionbingecalculator.app.TBC.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.TBC.Utils.CalculatorUtils;
import com.vanillax.televisionbingecalculator.app.TBC.Utils.IntentHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class ShowDetailsActivity extends Activity {

	@InjectView(R.id.poster_image)
	ImageView posterImage;

	@InjectView(R.id.seasons_count)
	TextView seasonsCountTextView;

	@InjectView(R.id.episode_total)
	TextView episdoeCountTextView;

	@InjectView( R.id.episode_length )
	TextView episodeRunTime;

	@InjectView(R.id.binge_time)
	TextView bingTimeText;

	@InjectView( R.id.show_title )
	TextView showTitle;

	protected String numberSeasons;
	protected String episodeCount;
	protected int runtime;
	protected String bingeTime;
	protected String imageUrl;
	protected String title;



	@OnClick( R.id.arrow )
	protected void goBack()
	{
		finish();
	}

	@OnCheckedChanged( R.id.commercial_checkbox )
	protected void checkChanged(boolean checked)
	{
		if(checked)
		{
			//WOW this is really hacky, gross, fix later...
			bingTimeText.setText( CalculatorUtils.calcBingeTimeWithNoCommercials( this, runtime, Integer.valueOf( episodeCount ), true ) );
		}
		else
		{
			bingTimeText.setText( CalculatorUtils.calcBingeTimeWithNoCommercials( this, runtime, Integer.valueOf( episodeCount ), false ) );
		}
	}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_show_details_material );
		ButterKnife.inject( this );

		numberSeasons = getIntent().getStringExtra( IntentHelper.NUMBER_SEASONS );
		episodeCount = getIntent().getStringExtra( IntentHelper.EPISDOE_COUNT );
		runtime = getIntent().getIntExtra( IntentHelper.EPISDOE_RUNTIME , 0  );
		bingeTime = getIntent().getStringExtra( IntentHelper.BINGE_TIME );
		imageUrl = getIntent().getStringExtra( IntentHelper.IMAGE_URL );
		title = getIntent().getStringExtra( IntentHelper.SHOW_TITLE );


		episodeRunTime.setText( "" + runtime );
		seasonsCountTextView.setText( numberSeasons );
		episdoeCountTextView.setText( episodeCount );
		bingTimeText.setText(  bingeTime );
		showTitle.setText( title );

		Picasso.with( getApplicationContext() )
				.load( imageUrl )
				.fit()
				.centerCrop()
				.into( posterImage );

    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

}
