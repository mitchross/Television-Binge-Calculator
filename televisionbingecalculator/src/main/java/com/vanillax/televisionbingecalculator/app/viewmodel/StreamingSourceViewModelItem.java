package com.vanillax.televisionbingecalculator.app.viewmodel;

import com.vanillax.televisionbingecalculator.app.R;

/**
 * Created by mitchross on 3/5/17.
 */

public class StreamingSourceViewModelItem
{
	public static final String NETFLIX = "netflix";
	public static final String HULU = "hulu";
	public static final String AMAZON = "amazon";
	public static final String VUDU = "vudu";
	public static final String HBO = "hbo";
	String streamSourceMovie;
	


	public StreamingSourceViewModelItem( String streamSource )
	{
		this.streamSourceMovie = streamSource;
	}


	public int getStreamingIcon()
	{
		String streamingIconName = "";
//		if( streamSource !=null)
//		{
//			streamingIconName = streamSource.sourceDisplayName.toLowerCase();
//		}
		 if ( streamSourceMovie !=null || !streamSourceMovie.isEmpty())
		{
			streamingIconName = streamSourceMovie.toLowerCase();
		}
		
			
		

		if( streamingIconName.contains( NETFLIX ))
        {

			return  R.drawable.netflix;
        }
        if ( streamingIconName.contains( HULU ))
        {
			return R.drawable.hulu;
        }
        if ( streamingIconName.contains( AMAZON ))
        {
			return R.drawable.amazon;
        }
        if ( streamingIconName.contains( VUDU ))
        {
			return R.drawable.vudu;
        }
		if ( streamingIconName.contains( HBO ))
		{
			return R.drawable.hbo;
		}
        else
		{
			return 0;
		}

	}
}
