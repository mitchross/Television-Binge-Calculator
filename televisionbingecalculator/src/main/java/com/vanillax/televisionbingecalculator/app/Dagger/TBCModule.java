package com.vanillax.televisionbingecalculator.app.Dagger;

import android.content.Context;

import com.vanillax.televisionbingecalculator.app.TBC.TelevisionBingeCalculator;

import java.io.File;
import java.io.IOException;

import dagger.Module;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by mitch on 5/27/14.
 *
 * API KEY fc4a94f471d92ae34988bf5e135bb25d
 */

@Module
(
		injects =
		{
				TelevisionBingeCalculator.class
		},

		complete = false,
		library = true
)


public class TBCModule
{
	public static final String API_KEY = "1e4890cd4f30dab070baf9672def4f17";
	public static final String BASE_IMAGE_PATH = "http://image.tmdb.org/t/p/w500/";
	public static final String BASE_IMAGE_PATH_LARGE = "http://image.tmdb.org/t/p/w1280/";



//
//	@Provides
//	@Singleton
//	TheMovieDbAPI providesTheMovieDbAPI ( @ForApplication Context context)
//	{
//
//		Retrofit restAdapter = new Retrofit.Builder()
//				.baseUrl( "http://api.themoviedb.org/3/" )
//				.client( getOkHttpClient( context ))
//				.addConverterFactory( GsonConverterFactory.create() )
//				.addCallAdapterFactory( RxJavaCallAdapterFactory.create())
//				.build();
//
//		return restAdapter.create( TheMovieDbAPI.class );
//	}


//	@Provides
//	@Singleton
//	ShowQueryMasterAPI providesShowQueryMasterAPI(  @ForApplication
//													Context context)
//	{
//
//		Retrofit restAdapter = new Retrofit.Builder()
//				.baseUrl( "https://api.themoviedb.org/3/" )
//				.client( getOkHttpClient( context ))
//				.addConverterFactory( GsonConverterFactory.create() )
//				.addCallAdapterFactory( RxJavaCallAdapterFactory.create())
//				.build();
//
//
//		return restAdapter.create( ShowQueryMasterAPI.class );
//
//	}



//	@Provides
//	@Singleton
//	TVBCLoggerAPI providesTVBCShowQueryMasterAPI(@ForApplication Context context)
//	{
//		OkHttpClient okHttpClient = new OkHttpClient();
//
//		Retrofit restAdapter = new Retrofit.Builder()
//				.baseUrl( "https://tvbc-logger.herokuapp.com/api/" )
//				.client( getOkHttpClient( context ))
//				.addConverterFactory( GsonConverterFactory.create() )
//				.addCallAdapterFactory( RxJavaCallAdapterFactory.create())
//				.build();
//
//
//		return restAdapter.create( TVBCLoggerAPI.class );
//	}


//	@Provides
//	@Singleton
//	JustWatchAPI providesJustWatchAPI( @ForApplication Context context)
//	{
//
//		Retrofit restAdapter = new Retrofit.Builder()
//				.baseUrl( "https://api.justwatch.com/" )
//				.client( getOkHttpClient( context ))
//				.addConverterFactory( GsonConverterFactory.create() )
//				.addCallAdapterFactory( RxJavaCallAdapterFactory.create())
//				.build();
//
//		return restAdapter.create( JustWatchAPI.class );
//	}




	private OkHttpClient getOkHttpClient( Context context)
	{
		long SIZE_OF_CACHE = 10 * 1024 * 1024; // 10 MiB
		Cache cache = new Cache(new File(context.getCacheDir(), "http"), SIZE_OF_CACHE);

		HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
		httpLoggingInterceptor.setLevel( HttpLoggingInterceptor.Level.BODY);


		return new OkHttpClient.Builder()
				.cache(cache)
				.addInterceptor( new RewriteRequestInterceptor() )
				.addInterceptor( httpLoggingInterceptor )
				.addNetworkInterceptor( new RewriteResponseCacheControlInterceptor() )
				.build();

	}


	public class RewriteRequestInterceptor implements Interceptor {
		@Override
		public Response intercept(Chain chain) throws IOException {
			int maxStale = 60 * 60 * 24 * 5;
			Request request;
			request = chain.request().newBuilder().header("Cache-Control", "max-stale=" + maxStale).build();
			return chain.proceed(request);
		}
	}

	public class RewriteResponseCacheControlInterceptor implements Interceptor {
		@Override
		public Response intercept(Chain chain) throws IOException {
			int maxStale = 60 * 60 * 24 * 5;
			Response originalResponse = chain.proceed(chain.request());
			return originalResponse.newBuilder().header("Cache-Control", "public, max-age=120, max-stale=" + maxStale).build();
		}
	}


}
