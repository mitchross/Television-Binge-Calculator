package com.vanillax.televisionbingecalculator.app.TBC.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.TBC.LandingActivityMain;

import butterknife.ButterKnife;
import butterknife.InjectView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.details2 );
		ButterKnife.inject( this );

		numberSeasons = getIntent().getStringExtra( LandingActivityMain.NUMBER_SEASONS );
		episodeCount = getIntent().getStringExtra( LandingActivityMain.EPISDOE_COUNT );
		runtime = getIntent().getIntExtra( LandingActivityMain.EPISDOE_RUNTIME , 0  );
		bingeTime = getIntent().getStringExtra( LandingActivityMain.BINGE_TIME );
		imageUrl = getIntent().getStringExtra( LandingActivityMain.IMAGE_URL );
		title = getIntent().getStringExtra( LandingActivityMain.SHOW_TITLE );


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
