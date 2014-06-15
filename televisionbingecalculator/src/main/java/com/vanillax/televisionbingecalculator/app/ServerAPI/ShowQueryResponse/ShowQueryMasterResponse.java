package com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mitch on 6/8/14.
 */
public class ShowQueryMasterResponse
{

	@SerializedName( "title" )
	public String title;

	@SerializedName( "runtime" )
	public int runtime;

	@SerializedName( "seasons" )
	public List<Seasons> seasons;

	@SerializedName( "images" )
	public ImageType images;

}
