package com.vanillax.televisionbingecalculator.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.vanillax.televisionbingecalculator.app.Models.SeasonSummaryResponse;

import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import roboguice.util.Ln;


public class LandingActivityMain extends Activity  {

	@Inject
	ShowInfoAPI showInfoAPI;

	List<SeasonSummaryResponse> testList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_activity_main);
		TelevisionBingeCalculator.inject( this );


    }

	@Override
	protected void onResume()
	{
		super.onResume();
		 showInfoAPI.searchShow( "Friends", new ShowSummaryResponseCallback() );
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

	protected class ShowSummaryResponseCallback implements Callback< List<SeasonSummaryResponse> >
	{

		@Override
		public void success( List<SeasonSummaryResponse> seasonSummaryResponse, Response response )
		{
			Ln.d( "Test" );
		}

		@Override
		public void failure( RetrofitError retrofitError )
		{
			Ln.d("fail");
		}
	}
}
