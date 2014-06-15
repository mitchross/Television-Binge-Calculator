package com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mitch on 6/8/14.
 */
public class Seasons
{
	@SerializedName( "season"  )
	public int seasonList;

	@SerializedName( "episodes" )
	public List<Episodes> episodesList;

}
