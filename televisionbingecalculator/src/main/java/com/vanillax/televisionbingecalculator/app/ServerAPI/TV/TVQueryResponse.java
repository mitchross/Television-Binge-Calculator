package com.vanillax.televisionbingecalculator.app.ServerAPI.TV;

import com.vanillax.televisionbingecalculator.app.TBC.Utils.CalculatorUtils;
import com.vanillax.televisionbingecalculator.app.viewmodel.ShowPosterViewModelItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitchross on 11/30/15.
 */
public class TVQueryResponse
{
	public List<ShowPosterListing> showPosterListings;


	public List<ShowPosterListing> getTVQueryResults()
	{
		return showPosterListings;
	}

	public List<ShowPosterViewModelItem> showsViewModelItems()
	{
		ArrayList<ShowPosterViewModelItem> shows = new ArrayList<ShowPosterViewModelItem>(  );




	}



	public ArrayList<String> getShowTitles()
	{
		ArrayList<String> showTitles = new ArrayList<String>();

		for ( ShowPosterListing showPosterListing : showPosterListings )
		{
			showTitles.add( showPosterListing.original_name );
		}

		return showTitles;
	}


	public ArrayList<String> getShowPosters()
	{
		ArrayList<String> showPosters = new ArrayList<String>();

		for ( ShowPosterListing showPosterListing : showPosterListings )
		{
			showPosters.add( CalculatorUtils.getShowPosterThumbnail( showPosterListing.posterPath, false ) );
		}

		return showPosters;
	}


	public int getCount()
	{
		return showPosterListings.size();
	}


}
