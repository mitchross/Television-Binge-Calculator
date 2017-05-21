package com.vanillax.televisionbingecalculator.app.ServerAPI.movie;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mitchross on 5/5/17.
 */

public class JustWatchShowResponse
{
	@SerializedName("items")
	public List<JustWatchSearchItem> items;


}
