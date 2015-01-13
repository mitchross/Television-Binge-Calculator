package com.vanillax.televisionbingecalculator.app.TBC.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse.Seasons;
import com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse.ShowQueryMasterResponse;

import roboguice.util.Ln;

/**
 * Created by mitch on 10/6/14.
 */
public class CalculatorUtils
{

	public static Intent calculateBingeTimeAndNavigate(Context context , ShowQueryMasterResponse myShow)
	{

		int runTime = myShow.runtime;
		int SeasonCount = myShow.seasons.size();
		String imageURL = myShow.images.fanart;
		String showTitle = myShow.title;

		int totalEpisodes = 0;

		for ( Seasons mySeason : myShow.seasons)
		{
			Ln.d( "Season " + mySeason.seasonList + "Size " + mySeason.episodesList.size() ) ;
			if (mySeason.seasonList != 0 )
			{
				totalEpisodes += mySeason.episodesList.size();
			}
		}


		String numberOfSeasons = ( "" + (  myShow.seasons.size() <= 1 ? 1 :  (myShow.seasons.size() - 1 )  ) );
		String episodeCount = ( "" +totalEpisodes );
		int totalBingTime = runTime * totalEpisodes;

		String bingeTime = convertToDaysHoursMins( context, totalBingTime );

		return IntentHelper.CreateShowDetailsIntent( context , numberOfSeasons , episodeCount , runTime , bingeTime , imageURL , showTitle );


	}


	public static String calcFineTuneTime(Context context, int totalEpisodes , int runtime ,  int openingCreditTime , int closingCreditTime , boolean hasCommercials )
	{
		int newRunTime = runtime;

		//Since we are taking out commercials use best guess
		if ( hasCommercials)
		{
			if ( newRunTime == 30 )
			{
				newRunTime = 22;
			}
			else if ( newRunTime == 60 )
			{
				newRunTime = 45;
			}

		}

		int runtimeMinusCredits =  newRunTime -  (openingCreditTime + closingCreditTime);

		int totalBingTime = runtimeMinusCredits * totalEpisodes;

		return convertToDaysHoursMins( context, totalBingTime );

	}


	public static String convertToDaysHoursMins( Context context, int timeInMinutes )
	{
		double  minutes, hours , days;
		final Resources resources = context.getResources();


		days =  Math.floor( timeInMinutes / 1440 );
		double temp = timeInMinutes - ( days * 1440 );
		hours =  Math.floor( temp / 60 );
		minutes =  (temp - ( hours * 60 ));

		String daysText = String.format( resources.getQuantityString( R.plurals.day,  (int) days ,  (int)days ));
		String hoursText = String.format(resources.getQuantityString( R.plurals.hours,  (int)hours, (int)hours ));
		String minsText = String.format(resources.getQuantityString( R.plurals.mins,  (int) minutes  , (int)minutes ));

		String result =   daysText + " " +  hoursText + " " +minsText;

		return result;


	}

	//A little risky hack to convert original listview poster images to a smaller version for performance
	//doing this until I switch to API v2
	public static String getShowPosterThumbnail ( String originalUrl )
	{
		if ( originalUrl !=null )
		{
			String newUrl = originalUrl.replace( "original", "thumb" );
			return newUrl;
		}
		return null;
	}
}
