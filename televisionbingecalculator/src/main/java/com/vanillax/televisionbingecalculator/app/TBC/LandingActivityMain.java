package com.vanillax.televisionbingecalculator.app.TBC;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryMasterAPI;
import com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse.Seasons;
import com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse.ShowQueryMasterResponse;
import com.vanillax.televisionbingecalculator.app.TBC.adapters.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import roboguice.util.Ln;


public class LandingActivityMain extends Activity  {



	@Inject
	ShowQueryMasterAPI showQueryMasterAPI;

	@InjectView(R.id.poster_image)
	ImageView posterImage;

	@InjectView(R.id.seasons_count)
	TextView seasonsCountTextView;

	@InjectView(R.id.episode_total)
	TextView episdoeCountTextView;

	@InjectView(R.id.binge_time)
	TextView bingTimeText;

	@InjectView( R.id.search_field )
	EditText searchField;

	@OnClick( R.id.search_button )
	protected void searchShow()
	{
		String showToSearch = searchField.getText().toString();
		showQueryMasterAPI.queryShow( showToSearch , true , new ShowQueryMasterResponseCallback() );
	}

	@InjectView( R.id.spinner )
	Spinner spinner;


	int runTime;
	int SeasonCount;
	String imageURL;
	int totalEpisodes = 0;
	int totalBingTime;
	ShowQueryMasterResponse myShow;
	SpinnerAdapter mySpinnerAdapter;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_landing_activity_main);
		TelevisionBingeCalculator.inject( this );
		ButterKnife.inject( this );




    }

	@Override
	protected void onResume()
	{
		super.onResume();


	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.landing_activity_main, menu);
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

	protected void updateViews()
	{
		runTime = myShow.runtime;
		SeasonCount = myShow.seasons.size();
		imageURL = myShow.images.posterUrl;

		totalEpisodes = 0;

		for ( Seasons mySeason : myShow.seasons)
		{
			Ln.d( "Season " + mySeason.seasonList + "Size " + mySeason.episodesList.size() ) ;
			totalEpisodes += mySeason.episodesList.size();
		}

		seasonsCountTextView.setText( "Seasons " + ( myShow.seasons.size() - 1 ) );
		episdoeCountTextView.setText( " Total Episodes of all seasons " +totalEpisodes );
		totalBingTime = runTime * totalEpisodes;
		bingTimeText.setText( "Total binge time hours " + totalBingTime / 60 );



		Picasso.with( getApplicationContext() ).load( imageURL ).into( posterImage  );

		spinner.setAdapter( mySpinnerAdapter );



	}


	public class ShowQueryMasterResponseCallback implements Callback< List<ShowQueryMasterResponse> >
	{

		@Override
		public void success( List<ShowQueryMasterResponse> showQueryMasterResponses, Response response )
		{
			myShow = showQueryMasterResponses.get( 0 );
			ArrayList<String> showTitles = new ArrayList<String>(  );
			ArrayList<String> showPosters =  new ArrayList<String>(  );




			for ( ShowQueryMasterResponse show : showQueryMasterResponses)
			{
				showTitles.add( show.title );
				showPosters.add( show.images.posterUrl );

			}



			mySpinnerAdapter = new SpinnerAdapter( getApplicationContext() ,R.layout.spinnerrow , showTitles , showPosters  );
			updateViews();

		}

		@Override
		public void failure( RetrofitError retrofitError )
		{

			Ln.d("fail");
		}
	}

}
