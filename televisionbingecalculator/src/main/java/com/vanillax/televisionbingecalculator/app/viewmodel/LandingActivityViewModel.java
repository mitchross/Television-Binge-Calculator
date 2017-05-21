package com.vanillax.televisionbingecalculator.app.viewmodel;

import com.vanillax.televisionbingecalculator.app.ServerAPI.TV.TVQueryResponse;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TVBCLogger.EmptyResponse;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TVBCLogger.SearchTerm;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TVBCLoggerAPI;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TheMovieDbAPI;
import com.vanillax.televisionbingecalculator.app.TBC.TelevisionBingeCalculator;
import com.vanillax.televisionbingecalculator.app.view.LandingActivityMain;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mitchross on 3/1/17.
 */

public class LandingActivityViewModel extends BaseViewModel
{

	@Inject
	TheMovieDbAPI theMovieDbAPI;

	@Inject
	TVBCLoggerAPI tvbcLoggerAPI;

	public interface LandingActivityViewCallback extends LifeCycle.View
	{
		void updateShowList(TVQueryResponse tvQueryResponse);
		void onItemTouch( int id, String url, String title, LandingActivityMain.SearchType searchType);
	}

	public LandingActivityViewModel()
	{
		TelevisionBingeCalculator.inject( this );
	}


	public void searchShow( String query, LandingActivityMain.SearchType searchType)
	{
		if ( searchType == LandingActivityMain.SearchType.TV )
		{

			theMovieDbAPI.queryTV( query )
					.subscribeOn( Schedulers.newThread() )
					.observeOn( AndroidSchedulers.mainThread() )
					.subscribe( new Subscriber<TVQueryResponse>()
					{
						@Override
						public void onCompleted()
						{

						}

						@Override
						public void onError( Throwable e )
						{
							//finish();
						}

						@Override
						public void onNext( TVQueryResponse response )
						{
							( (LandingActivityViewCallback) getViewCallback() ).updateShowList( response );
						}
					} );
		}
		else
		{
			theMovieDbAPI.queryMovie( query )
					.subscribeOn( Schedulers.newThread() )
					.observeOn( AndroidSchedulers.mainThread() )
					.subscribe( new Subscriber<TVQueryResponse>()
					{
						@Override
						public void onCompleted()
						{

						}

						@Override
						public void onError( Throwable e )
						{
							//finish();
						}

						@Override
						public void onNext( TVQueryResponse response )
						{
							( (LandingActivityViewCallback) getViewCallback() ).updateShowList( response );
						}
					} );
		}

	}

	public void logShow( String title, LandingActivityMain.SearchType searchType)
	{
		if ( title!=null )
		{
			String titleDetails;

			switch ( searchType )
			{
				case MOVIE:
					 titleDetails = "Movie: " + title;
					break;
				case TV:
					 titleDetails = "TV: " + title;
					break;
				default:
					 titleDetails = "TV: " + title;
					break;
			}



			tvbcLoggerAPI.postSearchTerm( new SearchTerm( titleDetails ) )
					.subscribeOn( Schedulers.newThread() )
					.observeOn( AndroidSchedulers.mainThread() )
					.subscribe( new Subscriber<EmptyResponse>()
					{
						@Override
						public void onCompleted()
						{

						}

						@Override
						public void onError( Throwable e )
						{

						}

						@Override
						public void onNext( EmptyResponse emptyResponse )
						{

						}
					} );
		}
	}

}
