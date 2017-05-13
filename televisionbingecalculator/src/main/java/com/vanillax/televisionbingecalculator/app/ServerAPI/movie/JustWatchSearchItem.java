package com.vanillax.televisionbingecalculator.app.ServerAPI.movie;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mitchross on 5/5/17.
 */

public class JustWatchSearchItem
{
	@SerializedName("title")
	public String title;

	@SerializedName("offers")
	public List<Offer> offers;


}
