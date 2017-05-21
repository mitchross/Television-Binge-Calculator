package com.vanillax.televisionbingecalculator.app.ServerAPI.TV;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitchross on 5/21/17.
 */

public class Cast
{
	@SerializedName( "name" )
	public String name;

	@SerializedName( "profile_path" )
	public String profilePath;
}
