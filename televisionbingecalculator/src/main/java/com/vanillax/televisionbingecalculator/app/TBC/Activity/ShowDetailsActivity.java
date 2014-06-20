package com.vanillax.televisionbingecalculator.app.TBC.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.TBC.LandingActivityMain;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ShowDetailsActivity extends Activity {

	@InjectView(R.id.poster_image)
	ImageView posterImage;

	@InjectView(R.id.seasons_count)
	TextView seasonsCountTextView;

	@InjectView(R.id.episode_total)
	TextView episdoeCountTextView;

	@InjectView(R.id.binge_time)
	TextView bingTimeText;


	int runTime;
	int SeasonCount;
	String imageURL;
	int totalEpisodes = 0;
	int totalBingTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_show_details);
		ButterKnife.inject( this );
		getActionBar().setDisplayHomeAsUpEnabled(true);



		Bundle extras = getIntent().getExtras();
		String numberSeasons = getIntent().getStringExtra( LandingActivityMain.NUMBER_SEASONS );
		String episodeCount = getIntent().getStringExtra( LandingActivityMain.EPISDOE_COUNT );
		int bingeTime = getIntent().getIntExtra( LandingActivityMain.BINGE_HOURS, 0 );
		String imageUrl = getIntent().getStringExtra( LandingActivityMain.IMAGE_URL );

		seasonsCountTextView.setText( numberSeasons );
		episdoeCountTextView.setText( episodeCount );
		bingTimeText.setText( "" + bingeTime );

		Picasso.with( getApplicationContext() )
				.load( imageUrl )
				.into( posterImage );

    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_details, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
			switch ( item.getItemId() )
			{
				case android.R.id.home:
					finish();
					return true;
			}

		return super.onOptionsItemSelected( item );
	}



}
