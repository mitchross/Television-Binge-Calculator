package com.vanillax.televisionbingecalculator.app.ServerAPI.TV;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mitchross on 5/21/17.
 */

public class CastResponse
{
	@SerializedName("id" )
	public int id;

	@SerializedName( "cast" )
	public List<Cast> castList;
}
