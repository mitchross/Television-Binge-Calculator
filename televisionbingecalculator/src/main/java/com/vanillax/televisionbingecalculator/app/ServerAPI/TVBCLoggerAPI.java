package com.vanillax.televisionbingecalculator.app.ServerAPI;

import com.vanillax.televisionbingecalculator.app.ServerAPI.TVBCLogger.EmptyResponse;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TVBCLogger.SearchTerm;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;


/**
 * Created by mitchross on 6/7/15.
 */
public interface TVBCLoggerAPI
{
	@Headers( "Content-type: application/json" )
	@POST( "searchterms" )
	Observable<EmptyResponse> postSearchTerm ( @Body SearchTerm searchTerm );
}
