package com.vanillax.televisionbingecalculator.app.viewmodel;

import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.ServerAPI.GuideBoxResponse.GuideBoxAvailableContentResponse;

/**
 * Created by mitchross on 3/5/17.
 */

public class StreamingSourceViewModelItem
{
	GuideBoxAvailableContentResponse.StreamSource streamSource;

	public StreamingSourceViewModelItem( GuideBoxAvailableContentResponse.StreamSource streamSource)
	{
		this.streamSource = streamSource;
	}

	public String getStreamName()
	{
		return streamSource.sourceDisplayName;
	}

	public int getStreamingIcon()
	{
		String streamingIconName;
		streamingIconName = streamSource.sourceDisplayName.toLowerCase();

		if( streamingIconName.contains( "netflix" ))
        {
			return  R.drawable.netflix;
        }
        if ( streamingIconName.contains( "hulu" ))
        {
			return R.drawable.hulu;
        }
        if ( streamingIconName.contains( "amazon" ))
        {
			return R.drawable.amazon;        }
        if ( streamingIconName.contains( "vudu" ))
        {
			return R.drawable.vudu;
        }
        else
		{
			return 0;
		}

	}
}
