package com.vanillax.televisionbingecalculator.app.TBC.Utils;

import android.content.Context;
import android.content.Intent;

import com.vanillax.televisionbingecalculator.app.view.ShowDetailsActivity;

/**
 * Created by mitch on 10/6/14.
 */
public class IntentHelper
{
	public static final String NUMBER_SEASONS = "NumberOfSeasons";
	public static final String EPISDOE_COUNT = "EpisodeCount";
	public static final String EPISDOE_RUNTIME = "EpisodeRuntime";
	public static final String BINGE_TIME = "bingeTime";
	public static final String IMAGE_URL = "imageURL";
	public static final String SHOW_TITLE = "showTitle";


	public static Intent CreateShowDetailsIntent ( Context context,
												   String numberOfSeasons,
												   String episodeCount,
												   int runTime,
												   String bingeTime,
												   String imageURL,
												   String showTitle)
	{
		Intent intent = new Intent( context, ShowDetailsActivity.class );
		intent.putExtra( NUMBER_SEASONS , numberOfSeasons );
		intent.putExtra( EPISDOE_COUNT, episodeCount );
		intent.putExtra( EPISDOE_RUNTIME , runTime );
		intent.putExtra( BINGE_TIME , bingeTime );
		intent.putExtra( IMAGE_URL, imageURL );
		intent.putExtra ( SHOW_TITLE , showTitle );
		return intent;
	}


}
