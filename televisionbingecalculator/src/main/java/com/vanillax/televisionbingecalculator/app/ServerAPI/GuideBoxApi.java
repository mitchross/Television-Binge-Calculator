package com.vanillax.televisionbingecalculator.app.ServerAPI;

import com.vanillax.televisionbingecalculator.app.ServerAPI.GuideBoxResponse.GuideBoxAvailableContentResponse;
import com.vanillax.televisionbingecalculator.app.ServerAPI.GuideBoxResponse.GuideBoxShowTranslatorResponse;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by mitchross on 11/6/16.
 */

public interface GuideBoxApi
{
	@Headers("Cache-Control: public, max-age=600, s-maxage=600")
	@GET( "search/id/themoviedb/{themoviedbid}"  )
	Observable<GuideBoxShowTranslatorResponse> translateTheMovieDBID ( @Path( "themoviedbid" ) String showId  );

	@Headers("Cache-Control: public, max-age=600, s-maxage=600")
	@GET( "show/{guideboxid}/available_content"  )
	Observable<GuideBoxAvailableContentResponse> getAvailableContent ( @Path( "guideboxid" ) String showId  );
}
