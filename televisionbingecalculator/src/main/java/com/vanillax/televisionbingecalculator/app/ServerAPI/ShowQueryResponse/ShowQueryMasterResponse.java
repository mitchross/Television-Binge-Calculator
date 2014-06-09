package com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mitch on 6/8/14.
 */
public class ShowQueryMasterResponse
{

	@SerializedName( "title" )
	String title;

	@SerializedName( "runtime" )
	int runtime;

	@SerializedName( "seasons" )
	List<Seasons> seasons;

}
