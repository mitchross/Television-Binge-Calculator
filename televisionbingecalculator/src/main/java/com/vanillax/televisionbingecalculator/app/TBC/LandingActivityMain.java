package com.vanillax.televisionbingecalculator.app.TBC;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryMasterAPI;
import com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse.ShowQueryMasterResponse;
import com.vanillax.televisionbingecalculator.app.TBC.Utils.CalculatorUtils;
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
		ShowQueryMasterResponse selectedShow;
		selectedShow = myShows.get( position );
		Intent intent =  CalculatorUtils.calculateBingeTimeAndNavigate( this, selectedShow );
		startActivity( intent );
	}


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this);
        setContentView( R.layout.activity_main_material );
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
