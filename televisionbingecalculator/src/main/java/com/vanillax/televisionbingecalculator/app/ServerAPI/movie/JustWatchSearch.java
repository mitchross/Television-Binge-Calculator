package com.vanillax.televisionbingecalculator.app.ServerAPI.movie;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitchross on 5/5/17.
 */

public class JustWatchSearch
{
	@SerializedName( "query" )
	public String justWatchSearchQuery;

	public JustWatchSearch( String justWatchSearchQuery )
	{
		this.justWatchSearchQuery = justWatchSearchQuery;
	}
}
