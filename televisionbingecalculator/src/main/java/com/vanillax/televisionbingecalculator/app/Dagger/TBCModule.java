package com.vanillax.televisionbingecalculator.app.Dagger;

import android.content.Context;

import com.vanillax.televisionbingecalculator.app.ServerAPI.GuideBoxApi;
import com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryMasterAPI;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TVBCLoggerAPI;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TheMovieDbAPI;
import com.vanillax.televisionbingecalculator.app.TBC.Activity.LandingActivityMain;
import com.vanillax.televisionbingecalculator.app.TBC.Activity.ShowDetailsActivity;
import com.vanillax.televisionbingecalculator.app.TBC.ShowManager;
import com.vanillax.televisionbingecalculator.app.TBC.TelevisionBingeCalculator;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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
				ShowDetailsActivity.class,
				TelevisionBingeCalculator.class,


		},

		complete = false,
		library = true
)


public class TBCModule
{
	public static final String API_KEY = "1e4890cd4f30dab070baf9672def4f17";
	public static final String BASE_IMAGE_PATH = "http://image.tmdb.org/t/p/w500/";
	public static final String BASE_IMAGE_PATH_LARGE = "http://image.tmdb.org/t/p/w1280/";

	public static final String GUIDE_BOX_API_KEY = "rKvQ35M0UKOUDTOz9tD1C7IObWtRh4py";



	@Provides
	@Singleton
	TheMovieDbAPI providesTheMovieDbAPI ( @ForApplication Context context)
	{

		Retrofit restAdapter = new Retrofit.Builder()
				.baseUrl( "http://api.themoviedb.org/3/" )
				.client( getOkHttpClient( context ))
				.addConverterFactory( GsonConverterFactory.create() )
				.addCallAdapterFactory( RxJavaCallAdapterFactory.create())
				.build();

		TheMovieDbAPI theMovieDbAPI = restAdapter.create( TheMovieDbAPI.class );
		return theMovieDbAPI;
	}


	@Provides
	@Singleton
	ShowQueryMasterAPI providesShowQueryMasterAPI(  @ForApplication
													Context context)
	{

		Retrofit restAdapter = new Retrofit.Builder()
				.baseUrl( "https://api.themoviedb.org/3/" )
				.client( getOkHttpClient( context ))
				.addConverterFactory( GsonConverterFactory.create() )
				.addCallAdapterFactory( RxJavaCallAdapterFactory.create())
				.build();


		ShowQueryMasterAPI showQueryMasterAPI = restAdapter.create( ShowQueryMasterAPI.class );
		return showQueryMasterAPI;

	}

	@Provides
	@Singleton
	ShowManager providesShowManager()
	{
		return new ShowManager( );
	}

	@Provides
	@Singleton
	TVBCLoggerAPI providesTVBCShowQueryMasterAPI(@ForApplication Context context)
	{
		OkHttpClient okHttpClient = new OkHttpClient();

		Retrofit restAdapter = new Retrofit.Builder()
				.baseUrl( "https://tvbc-logger.herokuapp.com/api/" )
				.client( getOkHttpClient( context ))
				.addConverterFactory( GsonConverterFactory.create() )
				.addCallAdapterFactory( RxJavaCallAdapterFactory.create())
				.build();


		TVBCLoggerAPI tvbcLoggerAPI = restAdapter.create( TVBCLoggerAPI.class );
		return tvbcLoggerAPI;
	}


	@Provides
	@Singleton
	GuideBoxApi providesGuideBoxAPI( @ForApplication Context context)
	{

		Retrofit restAdapter = new Retrofit.Builder()
				.baseUrl( "https://api-public.guidebox.com/v1.43/US/" + GUIDE_BOX_API_KEY + "/" )
				.client( getOkHttpClient( context ))
				.addConverterFactory( GsonConverterFactory.create() )
				.addCallAdapterFactory( RxJavaCallAdapterFactory.create())
				.build();

		GuideBoxApi guideBoxAPI = restAdapter.create( GuideBoxApi.class );
		return guideBoxAPI;
	}




	private OkHttpClient getOkHttpClient( Context context)
	{
		OkHttpClient.Builder okClientBuilder = new OkHttpClient.Builder();
		HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
		httpLoggingInterceptor.setLevel( HttpLoggingInterceptor.Level.BASIC);
		okClientBuilder.addInterceptor(httpLoggingInterceptor);



		final File baseDir = context.getCacheDir();
		if (baseDir != null)
		{
			final File cacheDir = new File(baseDir, "HttpResponseCache");

			okClientBuilder.cache(new Cache(cacheDir, 8L * 1024 * 1024));
		}
		okClientBuilder.connectTimeout(12, TimeUnit.SECONDS);
		okClientBuilder.readTimeout(12, TimeUnit.SECONDS);
		okClientBuilder.writeTimeout(12, TimeUnit.SECONDS);
		return okClientBuilder.build();
	}


}
