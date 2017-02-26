package com.vanillax.televisionbingecalculator.app.TBC;

import com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse.ShowQueryResponse;

/**
 * Created by mitchross on 5/26/15.
 */
public class ShowManager
{
	private ShowQueryResponse showQueryResponse;

	public void setShowQueryResponse( ShowQueryResponse showQueryResponse )
	{
		this.show = showQueryResponse;
	}

	public ShowQueryResponse getShowQueryResponse()
	{
		return show;
	}

	public void clearShow()
	{
		show = null;
	}
}
