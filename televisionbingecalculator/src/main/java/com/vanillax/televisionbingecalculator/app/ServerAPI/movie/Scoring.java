package com.vanillax.televisionbingecalculator.app.ServerAPI.movie;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitchross on 5/20/17.
 */

public class Scoring
{
	@SerializedName( "provider_type" )
	public String providerType;

	@SerializedName( "value" )
	public Double value;
}
