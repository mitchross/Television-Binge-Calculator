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
		this.showQueryResponse = showQueryResponse;
	}

	public ShowQueryResponse getShowQueryResponse()
	{
		return showQueryResponse;
	}

	public void clearShow()
	{
		showQueryResponse = null;
	}
}
