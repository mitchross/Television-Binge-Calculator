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
			Ln.d( "Season " + mySeason.getSeasonNumber() + "Size " + mySeason.getSeasonEpisodeCount() ) ;
			if ( mySeason.getSeasonNumber() != 0 )
			{
				totalEpisodes += mySeason.getSeasonEpisodeCount();
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

	public int  getRunTimeForSeason( int seasonNumber )
	{
		int totalEpisodes = getNumberOfEpisodesForSeason( seasonNumber );

		return totalEpisodes * runtime;

	}

	public int getNumberOfEpisodesForSeason( int seasonNumber )
	{
		for ( Seasons mySeason : seasons)
		{
			if ( mySeason.getSeasonNumber() == seasonNumber )
			{
				return mySeason.getSeasonEpisodeCount();
			}
		}
		return 0;
	}



	public String getTotalBingeTime( Context context )
	{

		int totalEpisodes = getEpisodeCount();

		int totalBingTime = runtime * totalEpisodes;

		String bingeTime = CalculatorUtils.convertToDaysHoursMins( context, totalBingTime );

		return bingeTime;

	}


}
