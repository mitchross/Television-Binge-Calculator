package com.vanillax.televisionbingecalculator.app.ServerAPI;

import com.vanillax.televisionbingecalculator.app.ServerAPI.GuideBoxResponse.GuideBoxAvailableContentResponse;
import com.vanillax.televisionbingecalculator.app.ServerAPI.GuideBoxResponse.GuideBoxShowTranslatorResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import static com.vanillax.televisionbingecalculator.app.Dagger.TBCModule.GUIDE_BOX_API_KEY;

/**
 * Created by mitchross on 11/6/16.
 */

public interface GuideBoxApi
{
	//@GET( "search/id/themoviedb/{themoviedbid}"  )
	@GET("search?api_key=" + GUIDE_BOX_API_KEY + "&type=movie&field=id&id_type=themoviedb")
	Observable<GuideBoxShowTranslatorResponse> translateTheMovieDBID ( @Query( "query" ) String showId  );

	@GET( "movies/{guideboxid}/available_content?api_key="+ GUIDE_BOX_API_KEY  )
	Observable<GuideBoxAvailableContentResponse> getAvailableContent ( @Path( "guideboxid" ) String showId  );

	@GET("search?api_key=" + GUIDE_BOX_API_KEY + "&type=show&field=id&id_type=themoviedb")
	Observable<GuideBoxShowTranslatorResponse> translateTheMovieDBID_TV ( @Query( "query" ) String showId  );

	@GET( "shows/{guideboxid}/available_content?api_key="+ GUIDE_BOX_API_KEY  )
	Observable<GuideBoxAvailableContentResponse> getAvailableContent_TV ( @Path( "guideboxid" ) String showId  );
}
