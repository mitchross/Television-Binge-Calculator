package com.vanillax.televisionbingecalculator.app.ServerAPI;

import com.vanillax.televisionbingecalculator.app.ServerAPI.movie.JustWatchSearch;
import com.vanillax.televisionbingecalculator.app.ServerAPI.movie.JustWatchShowResponse;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by mitchross on 5/5/17.
 */

public interface JustWatchAPI
{
	@Headers( { "content-type: application/json", "User-Agent: JustWatch Python client (github.com/dawoudt/JustWatchAPI)" })
	@POST( "titles/en_US/popular" )
	Observable<JustWatchShowResponse> getMovieStreamingSources ( @Body JustWatchSearch movieSearch );
}
