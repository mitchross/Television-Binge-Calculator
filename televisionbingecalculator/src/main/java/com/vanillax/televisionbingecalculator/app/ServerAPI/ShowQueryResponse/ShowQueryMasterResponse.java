package com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.vanillax.televisionbingecalculator.app.TBC.Utils.CalculatorUtils;

import java.util.ArrayList;

import roboguice.util.Ln;

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
	public ArrayList<Seasons> seasons;

	@SerializedName( "images" )
	public ImageType images;


	public String getNumberOfSeasons( )
	{
		return ( "" + (  seasons.size() <= 1 ? 1 :  (seasons.size() - 1 )  ) );
	}

	public int getEpisodeCount( )
	{
		int totalEpisodes = 0;

		for ( Seasons mySeason : seasons)
		{
			Ln.d( "Season " + mySeason.seasonList + "Size " + mySeason.episodesList.size() ) ;
			if (mySeason.seasonList != 0 )
			{
				totalEpisodes += mySeason.episodesList.size();
			}
		}

		return totalEpisodes;
	}

	public int getRunTime( )
	{
		return runtime;
	}

	public String getShowDetailImageUrl()
	{
		return images.fanart;
	}

	public String getShowTitle()
	{
		return title;
	}


	public String getTotalBingeTime( Context context )
	{

		int totalEpisodes = getEpisodeCount();

		int totalBingTime = runtime * totalEpisodes;

		String bingeTime = CalculatorUtils.convertToDaysHoursMins( context, totalBingTime );

		return bingeTime;

	}


}
