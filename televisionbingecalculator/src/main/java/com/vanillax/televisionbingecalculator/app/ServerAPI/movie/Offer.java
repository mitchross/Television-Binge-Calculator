package com.vanillax.televisionbingecalculator.app.ServerAPI.movie;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitchross on 5/5/17.
 */

public class Offer
{
	public static final int VUDU = 7;
	public static final int NETFLIX = 8;
	public static final int AMAZON = 10;
	public static final int HBO = 31;
	public static final int HULU=15;


	@SerializedName("provider_id")
	public int providerId;
}
