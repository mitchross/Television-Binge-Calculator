package com.vanillax.televisionbingecalculator.app.TBC;

import android.app.Application;

import com.vanillax.televisionbingecalculator.app.Dagger.TBCModule;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Created by mitch on 5/27/14.
 */
public class TelevisionBingeCalculator extends Application
{
	private static ObjectGraph objectGraph;

	public static void inject( Object object )
	{
		objectGraph.inject( object );
	}

	protected List<TBCModule> getModules()
	{
		return Arrays.asList(
				new TBCModule() );
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		objectGraph = ObjectGraph.create( getModules().toArray() );
		objectGraph.inject( this );
		objectGraph.injectStatics();

	}
}
