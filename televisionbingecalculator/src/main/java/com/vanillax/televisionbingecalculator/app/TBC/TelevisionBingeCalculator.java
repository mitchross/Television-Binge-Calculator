package com.vanillax.televisionbingecalculator.app.TBC;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.vanillax.televisionbingecalculator.app.Dagger.AndroidModule;
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
	protected static Context myContext;
	private FirebaseAnalytics mFirebaseAnalytics;

	public static void inject( Object object )
	{
		objectGraph.inject( object );
	}

	protected List<Object> getModules()
	{
		return Arrays.asList(
				new AndroidModule (this),
				new TBCModule() );
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		objectGraph = ObjectGraph.create( getModules().toArray() );
		objectGraph.inject( this );
		objectGraph.injectStatics();

		myContext = this.getApplicationContext();
		mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
		MultiDex.install( this );


	}

	public static Context getContext()
	{
		return myContext;
	}
}
