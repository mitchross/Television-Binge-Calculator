package com.vanillax.televisionbingecalculator.app.viewmodel;

import com.vanillax.televisionbingecalculator.app.ServerAPI.TV.ShowPosterListing;
import com.vanillax.televisionbingecalculator.app.TBC.Utils.CalculatorUtils;

/**
 * Created by mitchross on 2/6/17.
 */

public class ShowPosterViewModelItem
{
	ShowPosterListing showPosterListing;

	public ShowPosterViewModelItem( ShowPosterListing showPosterListing )
	{
		this.showPosterListing = showPosterListing;
	}

	public String getTitlez()
	{
		return showPosterListing.original_name != null
			   ? showPosterListing.original_name
			   : showPosterListing.movie_title;
	}

	public String getPosterUrl()
	{
		return CalculatorUtils.getShowPosterThumbnail( showPosterListing.posterPath, false );
	}

	public int getId()
	{
		return showPosterListing.id;
	}


}
