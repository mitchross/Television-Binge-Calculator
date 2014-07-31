package com.vanillax.televisionbingecalculator.app.TBC;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
import butterknife.Optional;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import roboguice.util.Ln;


public class LandingActivityMain extends Activity  {

	public static final String NUMBER_SEASONS = "NumberOfSeasons";
	public static final String EPISDOE_COUNT = "EpisodeCount";
	public static final String EPISDOE_RUNTIME = "EpisodeRuntime";
	public static final String BINGE_TIME = "bingeTime";
	public static final String IMAGE_URL = "imageURL";
	public static final String SHOW_TITLE = "showTitle";

	ListViewAdapter mySpinnerAdapter;
	ShowQueryMasterResponse myShow;
	List<ShowQueryMasterResponse> myShows;

	int runTime;
	int SeasonCount;
	String imageURL;
	int totalEpisodes = 0;
	int totalBingTime;
	String showTitle;


	@Inject
	ShowQueryMasterAPI showQueryMasterAPI;


	@InjectView( R.id.search_field )
	EditText searchField;

	@InjectView( R.id.progress_bar )
	SmoothProgressBar progressBar;



	@Optional
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
        setContentView( R.layout.main2);
		TelevisionBingeCalculator.inject( this );
		ButterKnife.inject( this );

		// set up the action listener for the text field
		searchField.setOnEditorActionListener( new EditText.OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction( final TextView searchTextView, int actionID, KeyEvent event )
			{
					progressBar.setVisibility( View.VISIBLE );
					Ln.d( "onEditorAction... searchTerm: %s", searchTextView.getText().toString() );
					String showToSearch = searchField.getText().toString();
					showQueryMasterAPI.queryShow( showToSearch, true, new ShowQueryMasterResponseCallback() );
					return true;
			}

		} );

    }

	@Override
	protected void onResume()
	{
		super.onResume();


	}



	protected void calculateBingeTimeAndNavigate(ShowQueryMasterResponse myShow)
	{

		runTime = myShow.runtime;
		SeasonCount = myShow.seasons.size();
		imageURL = myShow.images.posterUrl;
		showTitle = myShow.title;

		totalEpisodes = 0;

		for ( Seasons mySeason : myShow.seasons)
		{
			Ln.d( "Season " + mySeason.seasonList + "Size " + mySeason.episodesList.size() ) ;
			if (mySeason.seasonList != 0 )
			{
				totalEpisodes += mySeason.episodesList.size();
			}
			
		}



		String numberOfSeasons = ( "" + (  myShow.seasons.size() <= 1 ? 1 :  (myShow.seasons.size() - 1 )  ) );
		String episodeCount = ( "" +totalEpisodes );
		int totalBingTime = runTime * totalEpisodes;

		String bingeTime = convertToDaysHoursMins( totalBingTime );

		Intent intent = new Intent( getApplicationContext(), ShowDetailsActivity.class );
		intent.putExtra( NUMBER_SEASONS , numberOfSeasons );
		intent.putExtra( EPISDOE_COUNT, episodeCount );
		intent.putExtra( EPISDOE_RUNTIME , runTime );
		intent.putExtra( BINGE_TIME , bingeTime );
		intent.putExtra( IMAGE_URL, imageURL );
		intent.putExtra ( SHOW_TITLE , showTitle );
		startActivity( intent );



	}

	protected String convertToDaysHoursMins( int timeInMinutes )
	{
		double  minutes, hours , days;
		final Resources resources = getResources();


		days =  Math.floor( timeInMinutes / 1440 );
		double temp = timeInMinutes - ( days * 1440 );
		hours =  Math.floor( temp / 60 );
		minutes =  (temp - ( hours * 60 ));

		String daysText = String.format( resources.getQuantityString( R.plurals.day,  (int) days ,  (int)days ));
		String hoursText = String.format(resources.getQuantityString( R.plurals.hours,  (int)hours, (int)hours ));
		String minsText = String.format(resources.getQuantityString( R.plurals.mins,  (int) minutes  , (int)minutes ));

		String result =   daysText + " " +  hoursText + " " +minsText;

		return result;


	}



	public class ShowQueryMasterResponseCallback implements Callback< List<ShowQueryMasterResponse> >
	{

		@Override
		public void success( List<ShowQueryMasterResponse> showQueryMasterResponses, Response response )
		{
			progressBar.setVisibility( View.GONE );

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

		}

		@Override
		public void failure( RetrofitError retrofitError )
		{
			progressBar.setVisibility( View.GONE );
			Ln.d("fail");
		}
	}

}
