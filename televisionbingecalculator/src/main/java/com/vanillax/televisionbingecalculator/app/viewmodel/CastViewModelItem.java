package com.vanillax.televisionbingecalculator.app.viewmodel;

import com.vanillax.televisionbingecalculator.app.ServerAPI.TV.Cast;
import com.vanillax.televisionbingecalculator.app.TBC.Utils.CalculatorUtils;

/**
 * Created by mitchross on 5/21/17.
 */

public class CastViewModelItem
{
	Cast cast;


	public CastViewModelItem ( Cast cast)
	{
	 	this.cast = cast;
	}

	public String getCastImage()
	{
		return CalculatorUtils.getShowPosterThumbnail( cast.profilePath, false );
	}



	public String getCastName()
	{
		return cast.name;
	}


}
