package com.vanillax.televisionbingecalculator.app.ServerAPI;

import com.vanillax.televisionbingecalculator.app.ServerAPI.TVBCLogger.EmptyResponse;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TVBCLogger.SearchTerm;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by mitchross on 6/7/15.
 */
public interface TVBCLoggerAPI
{
	@Headers( "Content-type: application/json" )
	@POST( "/searchterms" )
	 void postSearchTerm ( @Body SearchTerm searchTerm, Callback<EmptyResponse> cb );
}
