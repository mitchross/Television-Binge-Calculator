package com.vanillax.televisionbingecalculator.app.ServerAPI.TV;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitchross on 2/21/17.
 */
public class ShowPosterListing
{
	public int id;

	@SerializedName( "poster_path" )
	public String posterPath;

	@SerializedName( "original_name" )
	public String original_name;

	@SerializedName( "original_title" )
	public String movie_title;
}
