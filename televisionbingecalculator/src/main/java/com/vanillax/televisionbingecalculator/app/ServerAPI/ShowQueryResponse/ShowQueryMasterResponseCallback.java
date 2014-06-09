package com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import roboguice.util.Ln;

/**
 * Created by mitch on 6/8/14.
 */


public class ShowQueryMasterResponseCallback implements Callback < List<ShowQueryMasterResponse> >
{

	@Override
	public void success( List<ShowQueryMasterResponse> showQueryMasterResponses, Response response )
	{
		Ln.d( "test" );
	}

	@Override
	public void failure( RetrofitError retrofitError )
	{
		Ln.d("fail");
	}
}