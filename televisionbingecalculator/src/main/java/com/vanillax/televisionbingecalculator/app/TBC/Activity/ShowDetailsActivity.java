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
		Bundle extras = getIntent().getExtras();
		String numberSeasons = getIntent().getStringExtra( LandingActivityMain.NUMBER_SEASONS );
		String episodeCount = getIntent().getStringExtra( LandingActivityMain.EPISDOE_COUNT );
		int bingeTime = getIntent().getIntExtra( LandingActivityMain.BINGE_HOURS, 0 );
		String imageUrl = getIntent().getStringExtra( LandingActivityMain.IMAGE_URL );

		seasonsCountTextView.setText( numberSeasons );
		episdoeCountTextView.setText( episodeCount );
		bingTimeText.setText( "Total binge time hours " + bingeTime );

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//	protected void updateViews()
//	{
//		runTime = myShow.runtime;
//		SeasonCount = myShow.seasons.size();
//		imageURL = myShow.images.posterUrl;
//
//		totalEpisodes = 0;
//
//		for ( Seasons mySeason : myShow.seasons)
//		{
//			Ln.d( "Season " + mySeason.seasonList + "Size " + mySeason.episodesList.size() ) ;
//			totalEpisodes += mySeason.episodesList.size();
//		}
//
//		seasonsCountTextView.setText( "Seasons " + ( myShow.seasons.size() - 1 ) );
//		episdoeCountTextView.setText( " Total Episodes of all seasons " +totalEpisodes );
//		totalBingTime = runTime * totalEpisodes;
//		bingTimeText.setText( "Total binge time hours " + totalBingTime / 60 );
//
//
//
//		Picasso.with( getApplicationContext() ).load( imageURL ).into( posterImage  );
//
//
//
//
//
//	}
}
