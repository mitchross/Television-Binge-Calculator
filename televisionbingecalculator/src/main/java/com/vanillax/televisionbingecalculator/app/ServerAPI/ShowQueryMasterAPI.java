package com.vanillax.televisionbingecalculator.app.ServerAPI;


import com.vanillax.televisionbingecalculator.app.Dagger.TBCModule;
import com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse.ShowQueryMasterResponse;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by mitch on 6/8/14.
 */
public interface ShowQueryMasterAPI
{
	@GET( "shows.json/" + TBCModule.API_KEY  )
	Observable<List<ShowQueryMasterResponse>> queryShow ( @Query( "query" ) String show , @Query( "seasons" ) boolean constantTrue  );

	@Headers( {"Content-Type: application/json", "trakt-api-version:2", "trakt-api-key:" + TBCModule.API_KEY })
	@GET( "shows/{show}/seasons" )
	Observable< List<ShowQueryMasterResponse> > autoCompleteQuery ( @Path("show")
																	String show );

}
