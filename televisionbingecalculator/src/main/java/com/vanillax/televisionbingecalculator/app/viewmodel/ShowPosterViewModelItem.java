package com.vanillax.televisionbingecalculator.app.viewmodel;

import com.vanillax.televisionbingecalculator.app.ServerAPI.TV.ShowPosterListing;

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
		return showPosterListing.original_name;
	}

	public String getPosterUrl()
	{
		return showPosterListing.posterPath;
	}


}
