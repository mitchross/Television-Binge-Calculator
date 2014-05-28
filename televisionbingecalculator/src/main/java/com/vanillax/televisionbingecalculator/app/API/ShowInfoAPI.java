package com.vanillax.televisionbingecalculator.app.API;

import com.vanillax.televisionbingecalculator.app.Dagger.TBCModule;
import com.vanillax.televisionbingecalculator.app.Models.SeasonSummary;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by mitch on 5/27/14.
 */
public interface ShowInfoAPI
{
	/**
	 * Get Show Summary
	 */

	//http://api.trakt.tv/search/shows.json/fc4a94f471d92ae34988bf5e135bb25d?query=big+bang+theory

	@GET( "/shows.json/" + TBCModule.API_KEY )
	List<SeasonSummary> searchShow ( @Query( "query" ) String showToSearch);
}
