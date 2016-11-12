package com.vanillax.televisionbingecalculator.app.ServerAPI.GuideBoxResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mitchross on 11/6/16.
 */

public class GuideBoxAvailableContentResponse
{
	@SerializedName( "results" )
	public Results results;


	/**
	 * Nested Heirarchy yuck, but thats how the JSON is returned
	 */
	protected class Results
	{
		@SerializedName( "web" )
		protected Web web;
	}

	protected class Web
	{
		@SerializedName( "episodes" )
		protected Episodes episodes;
	}

	protected class Episodes
	{
		@SerializedName( "all_sources" )
		List<StreamSource> streamSourceList;
	}

	public class StreamSource
	{
		@SerializedName( "display_name" )
		public String sourceDisplayName;
	}

	public List<StreamSource> getStreamSources()
	{
		return results.web.episodes.streamSourceList;
	}


}


