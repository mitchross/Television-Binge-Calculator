package com.vanillax.televisionbingecalculator.app.ServerAPI.TVBCLogger;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitchross on 6/7/15.
 */
public class SearchTerm
{
	@SerializedName( "text" )
	String searchTerm;

	public SearchTerm( String searchTerm )
	{
		this.searchTerm = searchTerm;
	}
}
