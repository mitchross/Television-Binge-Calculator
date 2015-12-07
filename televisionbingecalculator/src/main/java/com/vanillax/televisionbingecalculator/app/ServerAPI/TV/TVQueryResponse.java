package com.vanillax.televisionbingecalculator.app.ServerAPI.TV;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mitchross on 11/30/15.
 */
public class TVQueryResponse
{
	public List<Result> results;


	public class Result
	{
		public int id;

		@SerializedName( "poster_path" )
		public String posterPath;

		@SerializedName( "original_name" )
		public String original_name;
	}
}
