package com.vanillax.televisionbingecalculator.app.viewmodel;

import com.vanillax.televisionbingecalculator.app.ServerAPI.TV.ShowPosterListing;
import com.vanillax.televisionbingecalculator.app.TBC.Utils.CalculatorUtils;
import com.vanillax.televisionbingecalculator.app.view.LandingActivityMain;

/**
 * Created by mitchross on 2/6/17.
 */

public class ShowPosterViewModelItem
{
	ShowPosterListing showPosterListing;
	LandingActivityMain.SearchType searchType;

	public ShowPosterViewModelItem( ShowPosterListing showPosterListing, LandingActivityMain.SearchType searchType)
	{
		this.showPosterListing = showPosterListing;
		this.searchType = searchType;
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

	public LandingActivityMain.SearchType getSearchType()
	{
		return searchType;
	}


}
