package com.vanillax.televisionbingecalculator.app.Dagger;

import com.vanillax.televisionbingecalculator.app.API.ShowInfoAPI;
import com.vanillax.televisionbingecalculator.app.LandingActivityMain;
import com.vanillax.televisionbingecalculator.app.TelevisionBingeCalculator;

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
		}
)


public class TBCModule
{
	public static final String API_KEY = "fc4a94f471d92ae34988bf5e135bb25d";

	@Provides
	@Singleton
	ShowInfoAPI providesShowInfoAPI()
	{
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint( "http://api.trakt.tv/search" )
				.setLogLevel( RestAdapter.LogLevel.FULL )
				.build();

		ShowInfoAPI showInfoAPI = restAdapter.create( ShowInfoAPI.class );

		return showInfoAPI;
	}


}
