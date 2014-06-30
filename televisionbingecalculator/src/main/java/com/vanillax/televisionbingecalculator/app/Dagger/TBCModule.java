package com.vanillax.televisionbingecalculator.app.Dagger;

import android.content.Context;

import com.squareup.okhttp.HttpResponseCache;
import com.squareup.okhttp.OkHttpClient;
import com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryMasterAPI;
import com.vanillax.televisionbingecalculator.app.TBC.LandingActivityMain;
import com.vanillax.televisionbingecalculator.app.TBC.TelevisionBingeCalculator;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import roboguice.util.Ln;

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

		complete = false,
		library = true
)


public class TBCModule
{
	public static final String API_KEY = "fc4a94f471d92ae34988bf5e135bb25d";


	@Provides
	@Singleton
	ShowQueryMasterAPI providesShowQueryMasterAPI(  @ForApplication
													Context context)
	{

		OkHttpClient okHttpClient = new OkHttpClient();
		File cacheDir = new File( context.getCacheDir(), UUID.randomUUID().toString() );

		HttpResponseCache cache = null;
		try {
			cache = new HttpResponseCache(cacheDir, 8L * 1024 * 1024);
		} catch (IOException e) {
			Ln.e( e );
		}

		okHttpClient.setResponseCache( cache );

		RestAdapter restAdapter = new RestAdapter.Builder()
				.setClient( new OkClient(okHttpClient) )
				.setEndpoint( "http://api.trakt.tv/search" )
				.setLogLevel( RestAdapter.LogLevel.FULL )
				.build();
		ShowQueryMasterAPI showQueryMasterAPI = restAdapter.create( ShowQueryMasterAPI.class );
		return showQueryMasterAPI;

	}




}
