package com.vanillax.televisionbingecalculator.app.TBC;

import com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse.ShowQueryMasterResponse;

/**
 * Created by mitchross on 5/26/15.
 */
public class ShowManager
{
	private ShowQueryMasterResponse show;

	public void setShow( ShowQueryMasterResponse show)
	{
		this.show = show;
	}

	public ShowQueryMasterResponse getShow()
	{
		return show;
	}

	public void clearShow()
	{
		show = null;
	}
}
