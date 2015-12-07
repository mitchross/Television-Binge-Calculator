package com.vanillax.televisionbingecalculator.app.ServerAPI;

import com.vanillax.televisionbingecalculator.app.Dagger.TBCModule;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TV.TVQueryResponse;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TV.TVShowByIdResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by mitchross on 11/30/15.
 */
public interface TheMovieDbAPI
{
	@Headers("Cache-Control: public, max-age=600, s-maxage=600")
	@GET( "/search/tv?" + "api_key=" + TBCModule.API_KEY )
	public void queryShow ( @Query( "query" ) String show , Callback<TVQueryResponse> callback );

	@Headers("Cache-Control: public, max-age=600, s-maxage=600")
	@GET( "/tv/{showId}" + "?api_key=" + TBCModule.API_KEY  )
	public void queryShowDetails ( @Path( "showId" ) String showId , Callback<TVShowByIdResponse> callback );

	@Headers("Cache-Control: public, max-age=600, s-maxage=600")
	@GET( "/search/tv?" + "api_key=" + TBCModule.API_KEY )
	Observable<TVQueryResponse> queryShow ( @Query( "query" ) String show  );

	@Headers("Cache-Control: public, max-age=600, s-maxage=600")
	@GET( "/tv/{showId}" + "?api_key=" + TBCModule.API_KEY  )
	Observable<TVShowByIdResponse> queryShowDetails ( @Path( "showId" ) String showId  );


}