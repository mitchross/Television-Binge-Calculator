package com.vanillax.televisionbingecalculator.app.ServerAPI.TV;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import roboguice.util.Ln;

/**
 * Created by mitchross on 11/30/15.
 */
public class TVShowByIdResponse
{
	@SerializedName( "backdrop_path" )
	public String imageUrl;

	@SerializedName( "episode_run_time" )
	int[] episodeRunTimeArray;

	@SerializedName( "original_name" )
	public String title;

	@SerializedName( "overview" )
	public String episodeDescription;

	@SerializedName( "number_of_seasons" )
	public int numberOfSeasons;

	@SerializedName( "number_of_episodes" )
	public int numberOfEpisodes;

	public List<Seasons> seasons;


	public int getRunTimeAverage()
	{
		if ( episodeRunTimeArray.length == 0 )
		{
			return 0;
		}
		int sum = 0;
		for ( int i : episodeRunTimeArray )
		{
			sum += i;
		}

		return sum / episodeRunTimeArray.length;

	}

	public int numberOfSeasons()
	{

		return seasons.size() == 1 ? 1 : seasons.size() - 1;
	}

	public int getEpisodeCount( )
	{
		int totalEpisodes = 0;

		for ( Seasons s : seasons)
		{
			Ln.d( "Season " + s.seasonNumber + "Size " + s.episodeCount ) ;
			if ( s.seasonNumber != 0 )
			{
				totalEpisodes += s.episodeCount;
			}
		}

		return totalEpisodes;
	}

	public int  getRunTimeForSeason( int seasonNumber )
	{
		int totalEpisodes = getNumberOfEpisodesForSeason( seasonNumber );

		return totalEpisodes * getRunTimeAverage();

	}

	public int getNumberOfEpisodesForSeason( int seasonNumber )
	{
		for ( Seasons s : seasons)
		{
			if ( s.seasonNumber == seasonNumber + 1 )
			{
				return s.episodeCount;
			}
		}
		return 0;
	}

	public class Seasons
	{
		@SerializedName( "season_number" )
		public int seasonNumber;

		@SerializedName( "episode_count" )
		public int episodeCount;

		@SerializedName( "poster_path" )
		public String posterPath;
	}
}
