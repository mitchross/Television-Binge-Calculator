package com.vanillax.televisionbingecalculator.app.TBC;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryMasterAPI;
import com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse.Seasons;
import com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse.ShowQueryMasterResponse;
import com.vanillax.televisionbingecalculator.app.TBC.Activity.ShowDetailsActivity;
import com.vanillax.televisionbingecalculator.app.TBC.adapters.ListViewAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import roboguice.util.Ln;


public class LandingActivityMain extends Activity  {

	public static final String NUMBER_SEASONS = "NumberOfSeasons";
	public static final String EPISDOE_COUNT = "EpisodeCount";
	public static final String BINGE_HOURS = "bingHours";
	public static final String IMAGE_URL = "imageURL";

	ListViewAdapter mySpinnerAdapter;
	ShowQueryMasterResponse myShow;
	List<ShowQueryMasterResponse> myShows;

	int runTime;
	int SeasonCount;
	String imageURL;
	int totalEpisodes = 0;
	int totalBingTime;



	@Inject
	ShowQueryMasterAPI showQueryMasterAPI;



	@InjectView( R.id.search_field )
	EditText searchField;

	@OnClick( R.id.search_button )
	protected void searchShow()
	{
		String showToSearch = searchField.getText().toString();
		showQueryMasterAPI.queryShow( showToSearch , true , new ShowQueryMasterResponseCallback() );
	}

	@InjectView( R.id.list_view )
	ListView listView;

	@OnItemClick(R.id.list_view) void onItemClick(int position)
	{
		Toast.makeText( this, "You clicked: " + mySpinnerAdapter.getItem( position ), Toast.LENGTH_SHORT ).show();
		ShowQueryMasterResponse selectedShow;
		selectedShow = myShows.get( position );
		calculateBingeTimeAndNavigate( selectedShow );
	}


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

	protected void calculateBingeTimeAndNavigate(ShowQueryMasterResponse myShow)
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

		String numberOfSeasons = ( "Seasons " + ( myShow.seasons.size() - 1 ) );
		String episodeCount = ( " Total Episodes of all seasons " +totalEpisodes );
		int totalBingTime = runTime * totalEpisodes;
 		int bingHours = ( totalBingTime / 60 );

		Intent intent = new Intent( getApplicationContext(), ShowDetailsActivity.class );
		intent.putExtra( NUMBER_SEASONS , numberOfSeasons );
		intent.putExtra( EPISDOE_COUNT, episodeCount );
		intent.putExtra( BINGE_HOURS , bingHours );
		intent.putExtra( IMAGE_URL, imageURL );
		startActivity ( intent );



	}




	public class ShowQueryMasterResponseCallback implements Callback< List<ShowQueryMasterResponse> >
	{

		@Override
		public void success( List<ShowQueryMasterResponse> showQueryMasterResponses, Response response )
		{
			myShows = showQueryMasterResponses;

			ArrayList<String> showTitles = new ArrayList<String>(  );
			ArrayList<String> showPosters =  new ArrayList<String>(  );


			for ( ShowQueryMasterResponse show : showQueryMasterResponses)
			{
				showTitles.add( show.title );
				showPosters.add( show.images.posterUrl );

			}

			mySpinnerAdapter = new ListViewAdapter( getApplicationContext() , R.layout.spinnerrow , showTitles , showPosters  );
			listView.setAdapter( mySpinnerAdapter );
			//updateViews();

		}

		@Override
		public void failure( RetrofitError retrofitError )
		{

			Ln.d("fail");
		}
	}

}
