package com.vanillax.televisionbingecalculator.app.TBC;

import android.app.Application;
import android.content.Context;

import com.vanillax.televisionbingecalculator.app.Dagger.AndroidModule;
import com.vanillax.televisionbingecalculator.app.Dagger.TBCModule;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by mitch on 5/27/14.
 */
public class TelevisionBingeCalculator extends Application
{
	private static ObjectGraph objectGraph;
	protected static Context myContext;

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

		CalligraphyConfig.initDefault( "fonts/Roboto-Regular.ttf" );


	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}

	public static Context getContext()
	{
		return myContext;
	}
}
