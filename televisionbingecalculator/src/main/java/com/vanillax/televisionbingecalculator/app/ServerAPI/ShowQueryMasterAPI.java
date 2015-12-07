package com.vanillax.televisionbingecalculator.app.ServerAPI;


import com.vanillax.televisionbingecalculator.app.Dagger.TBCModule;
import com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse.ShowQueryMasterResponse;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;


/**
 * Created by mitch on 6/8/14.
 */
public interface ShowQueryMasterAPI
{
	@Headers("Cache-Control: public, max-age=600, s-maxage=600")
	@GET( "/shows.json/" + TBCModule.API_KEY  )
	public void queryShow ( @Query( "query" ) String show , @Query( "seasons" ) boolean constantTrue  , Callback<List<ShowQueryMasterResponse>> callback );

	@Headers( {"Content-Type: application/json", "trakt-api-version:2","Cache-Control: public, max-age=600, s-maxage=600", "trakt-api-key:" + TBCModule.API_KEY })
	@GET( "/shows/{show}/seasons" )
	Observable< List<ShowQueryMasterResponse> > autoCompleteQuery ( @Path("show")
																	String show );

}
