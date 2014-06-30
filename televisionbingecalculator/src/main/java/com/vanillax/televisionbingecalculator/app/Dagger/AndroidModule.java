package com.vanillax.televisionbingecalculator.app.Dagger;

import android.content.Context;

import com.vanillax.televisionbingecalculator.app.TBC.TelevisionBingeCalculator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mitch on 6/22/14.
 */


@Module (library = true)
public class AndroidModule
{
	private final TelevisionBingeCalculator myApplication;

	public AndroidModule( TelevisionBingeCalculator myApplication )
	{
		this.myApplication = myApplication;
	}

	@Provides
	@Singleton
	@ForApplication
	Context provideApplicationContext()
	{
		return myApplication;
	}

}
