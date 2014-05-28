package com.vanillax.televisionbingecalculator.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.vanillax.televisionbingecalculator.app.API.ShowInfoAPI;
import com.vanillax.televisionbingecalculator.app.Models.SeasonSummary;

import java.util.List;

import javax.inject.Inject;


public class LandingActivityMain extends Activity {

	@Inject
	ShowInfoAPI showInfoAPI;

	List<SeasonSummary> testList;

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
		//testList = showInfoAPI.searchShow( "Friends" );
		//Log.d("Mitch", testList.toString());
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
}
