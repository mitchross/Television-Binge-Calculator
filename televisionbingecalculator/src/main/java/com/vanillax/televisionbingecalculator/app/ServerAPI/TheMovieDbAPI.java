package com.vanillax.televisionbingecalculator.app.ServerAPI;


import com.vanillax.televisionbingecalculator.app.Dagger.TBCModule;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TV.CastResponse;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TV.TVQueryResponse;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TV.TVShowByIdResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mitchross on 11/30/15.
 */
public interface TheMovieDbAPI
{

	@GET( "search/tv?" + "api_key=" + TBCModule.API_KEY )
	Observable<TVQueryResponse> queryTV ( @Query( "query" ) String show  );

	@GET( "tv/{showId}" + "?api_key=" + TBCModule.API_KEY  )
	Observable<TVShowByIdResponse> queryTVDetails ( @Path( "showId" ) String showId  );

	@GET( "{searchType}/{showId}/credits" + "?api_key=" + TBCModule.API_KEY  )
	Observable<CastResponse> queryCast ( @Path( "showId" ) String showId, @Path( "searchType" ) String searchType );

	@GET( "search/movie?" + "api_key=" + TBCModule.API_KEY )
	Observable<TVQueryResponse> queryMovie ( @Query( "query" ) String show  );

	@GET( "movie/{showId}" + "?api_key=" + TBCModule.API_KEY  )
	Observable<TVShowByIdResponse> queryMovieDetails ( @Path( "showId" ) String showId  );

	@GET( "movie/{showId}/credits" + "?api_key=" + TBCModule.API_KEY  )
	Observable<CastResponse> queryMovieCast ( @Path( "showId" ) String showId  );




}
