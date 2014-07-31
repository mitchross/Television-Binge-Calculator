package com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitch on 6/14/14.
 */
public class ImageType
{
	@SerializedName( "poster" )
	public String posterUrl;

	@SerializedName( "fanart" )
	public String fanArt;
}
