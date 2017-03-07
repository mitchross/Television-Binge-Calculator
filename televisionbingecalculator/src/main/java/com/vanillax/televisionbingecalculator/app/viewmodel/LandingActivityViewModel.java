package com.vanillax.televisionbingecalculator.app.viewmodel;

import com.vanillax.televisionbingecalculator.app.ServerAPI.TV.TVQueryResponse;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TVBCLoggerAPI;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TheMovieDbAPI;
import com.vanillax.televisionbingecalculator.app.TBC.TelevisionBingeCalculator;

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
		void onItemTouch(int id, String url);
	}

	public LandingActivityViewModel()
	{
		TelevisionBingeCalculator.inject( this );
	}


	public void searchShow( String query)
	{

		theMovieDbAPI.queryShow( query )
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
						((LandingActivityViewCallback)getViewCallback()).updateShowList( response );
					}
				} );

	}

}
