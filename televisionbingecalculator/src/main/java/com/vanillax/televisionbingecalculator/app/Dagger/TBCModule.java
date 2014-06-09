package com.vanillax.televisionbingecalculator.app.Dagger;

import com.vanillax.televisionbingecalculator.app.TBC.LandingActivityMain;
import com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryMasterAPI;
import com.vanillax.televisionbingecalculator.app.TBC.TelevisionBingeCalculator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

/**
 * Created by mitch on 5/27/14.
 *
 * API KEY fc4a94f471d92ae34988bf5e135bb25d
 */

@Module
(
		injects =
		{
				LandingActivityMain.class,
				TelevisionBingeCalculator.class

		},

		complete = false
)


public class TBCModule
{
	public static final String API_KEY = "fc4a94f471d92ae34988bf5e135bb25d";


	@Provides
	@Singleton
	ShowQueryMasterAPI providesShowQueryMasterAPI()
	{
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint( "http://api.trakt.tv/search" )
				.setLogLevel( RestAdapter.LogLevel.FULL )
				.build();
		ShowQueryMasterAPI showQueryMasterAPI = restAdapter.create( ShowQueryMasterAPI.class );
		return showQueryMasterAPI;

	}


}
